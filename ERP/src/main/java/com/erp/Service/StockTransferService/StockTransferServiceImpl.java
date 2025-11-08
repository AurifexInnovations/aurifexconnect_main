package com.erp.Service.StockTransferService;

import com.erp.Dto.Request.*;
import com.erp.CustomRepository.StockTransferCustomRepository;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StockTransferResponse;
import com.erp.Enum.Action;
import com.erp.Enum.StockTransferStatus;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.StockTransfer_Exception.StockTransferNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Mapper.StockTransfer.StockTransferMapper;
import com.erp.Model.Branch;
import com.erp.Model.Inventory;
import com.erp.Model.StockTransfer;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.StockTransfer.StockTransferRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.Activity.ActivityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StockTransferServiceImpl implements StockTransferService
{
    private final StockTransferCustomRepository stockTransferCustomRepository;
    private final StockTransferRepository stockTransferRepository;
    private final StockTransferMapper stockTransferMapper;
    private final BranchRepository branchRepository;
    private final InventoryRepository inventoryRepository;

    private final UserIdentity userIdentity;

    private final ActivityService activityService;

    @Override
    public StockTransferResponse createStockTransfer(StockTransferRequest request) {

        Branch fromBranch = branchRepository.findById(request.getFromBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("From Branch not found!"));

        Branch toBranch = branchRepository.findById(request.getToBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("To Branch not found!"));

        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found!"));

        StockTransfer transfer = stockTransferMapper.mapToStockTransfer(request);
        transfer.setFromBranch(fromBranch);
        transfer.setToBranch(toBranch);
        transfer.setInventory(inventory);

        // note it
        transfer.setInitiatedBy(userIdentity.getCurrentUsername()); // This Field's values changes After Role Based Authentication
        transfer.setStatus(StockTransferStatus.PENDING);

        stockTransferRepository.save(transfer);

        ActivityDto activityDto=new ActivityDto();
        activityDto.setAction(Action.ADD_STOCK_TRANSFER.toString());
        activityDto.setInventoryId(request.getInventoryId());
        activityDto.setPerformedBy(userIdentity.getCurrentUsername());
        activityService.addActivity(request.getInventoryId(),activityDto);

        return stockTransferMapper.mapToStockTransferResponse(transfer);
    }

    @Override
    public StockTransferResponse approveTransfer(TransferActionRequest request) {
        StockTransfer transfer = stockTransferRepository.findById(request.getTransferId())
                .orElseThrow(() -> new StockTransferNotFoundException("Transfer not found!"));

        if (transfer.getStatus() != StockTransferStatus.PENDING) {
            throw new StockTransferNotFoundException("Transfer is already processed!");
        }

        // Reduce from fromBranch inventory
        Inventory fromInventory = transfer.getInventory();
        if (fromInventory.getItemQuantity() < transfer.getQuantity()) {
            throw new InventoryNotFoundException("Insufficient stock in source branch!");
        }
        fromInventory.setItemQuantity(fromInventory.getItemQuantity() - transfer.getQuantity());
        inventoryRepository.save(fromInventory);

        // Increase to toBranch inventory or create if not present
        Branch toBranch = transfer.getToBranch();
        Inventory toInventory = inventoryRepository.findByBranchAndItemName(toBranch, fromInventory.getItemName())
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setItemName(fromInventory.getItemName());
                    newInventory.setItemCost(fromInventory.getItemCost());
                    newInventory.setCategories(fromInventory.getCategories());
                    newInventory.setBranch(toBranch);
                    newInventory.setItemDescription(fromInventory.getItemDescription());
                    newInventory.setLowStockThreshold(fromInventory.getLowStockThreshold());
                    newInventory.setItemQuantity(0.0);
                    return newInventory;
                });

        toInventory.setItemQuantity(toInventory.getItemQuantity() + transfer.getQuantity());
        inventoryRepository.save(toInventory);

        // Update transfer status & approver
        transfer.setStatus(StockTransferStatus.APPROVED);
        transfer.setApprovedBy(request.getApproverName());
        stockTransferRepository.save(transfer);

        return stockTransferMapper.mapToStockTransferResponse(transfer);
    }

    @Override
    public StockTransferResponse rejectTransfer(TransferActionRequest request) {
        StockTransfer transfer = stockTransferRepository.findById(request.getTransferId())
                .orElseThrow(() -> new StockTransferNotFoundException("Transfer not found!"));

        if (transfer.getStatus() != StockTransferStatus.PENDING) {
            throw new StockTransferNotFoundException("Transfer is already processed!");
        }

        transfer.setStatus(StockTransferStatus.REJECTED);
        transfer.setApprovedBy(request.getApproverName());
        stockTransferRepository.save(transfer);

        return stockTransferMapper.mapToStockTransferResponse(transfer);
    }

    @Override
    public List<StockTransferResponse> getAllTransfers(PaginationRequest request) {
        List<StockTransfer> transfers;

        if(request.getPageNumber() != null && request.getPageSize() != null){
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
            Page<StockTransfer> pageResult = stockTransferRepository.findAll(pageable);
            transfers = pageResult.getContent();
        }else{
            transfers = stockTransferRepository.findAll();
        }
        return stockTransferMapper.mapToStockTransferResponse(transfers);
    }

    @Override
    public List<StockTransferResponse> getTransfersByStatus(StockTransferParam param) {
        List<StockTransfer> transfers = stockTransferRepository.findByStatus(param.getStatus());

        if (transfers.isEmpty()) {
            throw new StockTransferNotFoundException("No Transfers Found with Status: " + param.getStatus());
        }

        return stockTransferMapper.mapToStockTransferResponse(transfers);
    }


    @Override
    public ResultDto<StockTransferResponse> getStockTransferDetails(FilterRequest filterRequest)
    {
        ResultDto<StockTransferResponse> transferResponses = stockTransferCustomRepository.getStockTransferDetails(filterRequest);

        if(transferResponses.getResults().isEmpty())
        {
            throw new StockTransferNotFoundException("No Transfer Found With Status");
        }

        return transferResponses;
    }


    @Transactional
    public void updateStockTransferStatus(long itemId, StockTransferStatus status) {

        log.info("Updating transfer status for itemId={} to {}", itemId, status);

        try {
            stockTransferRepository.updateStatusByItemId(itemId, status);
            log.info("Successfully updated status for itemId={}", itemId);

        } catch (Exception e) {
            log.error("Error updating status for itemId={}", itemId, e);
            throw e;
        }
    }

}
