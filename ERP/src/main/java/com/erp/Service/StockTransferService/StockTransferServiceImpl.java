package com.erp.Service.StockTransferService;

import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Request.StockTransferParam;
import com.erp.Dto.Request.StockTransferRequest;
import com.erp.Dto.Request.TransferActionRequest;
import com.erp.Dto.Response.StockTransferResponse;
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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StockTransferServiceImpl implements StockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final StockTransferMapper stockTransferMapper;
    private final BranchRepository branchRepository;
    private final InventoryRepository inventoryRepository;

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
        transfer.setStatus(StockTransferStatus.PENDING);

        stockTransferRepository.save(transfer);
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

}
