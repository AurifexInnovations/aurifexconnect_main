package com.erp.Service.InventoryService;

import com.erp.CustomRepository.InventoryCustomRepository;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.InventoryRequest;
import com.erp.Dto.Response.InventoryResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StockValueResponse;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Inventory.InventoryMapper;
import com.erp.Model.Branch;
import com.erp.Model.Inventory;
import com.erp.Model.Tax;
import com.erp.Projection.InventoryAndBranchProjection;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Tax.TaxRepository;
import com.erp.Utility.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final BranchRepository branchRepository;
    private final TaxRepository taxRepository;

    private final InventoryCustomRepository inventoryCustomRepository;

    @Override
    public InventoryResponse addItem(InventoryRequest inventoryRequest) {
        Branch branch = branchRepository.findById(inventoryRequest.getBranchAndInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch Not Found, Invalid Id"));

        Inventory inventory = inventoryMapper.mapToInventory(inventoryRequest);
        List<Tax> taxes = inventoryRequest.getApplicableTaxNames().stream()
                .map(taxName -> taxRepository.findByTaxName(taxName)
                        .orElseThrow(() -> new IllegalArgumentException("Tax not found: " + taxName)))
                .collect(Collectors.toList());
        inventory.setTaxes(taxes);

        inventory.setBranch(branch);
        inventoryRepository.save(inventory);
        return inventoryMapper.mapToInventoryResponse(inventory);
    }

    @Override
    public InventoryResponse updateItem(InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.findById(inventoryRequest.getBranchAndInventoryId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found , invalid id "));

        inventoryMapper.mapToInventoryEntity(inventoryRequest, inventory);

        inventoryRepository.save(inventory);
        return inventoryMapper.mapToInventoryResponse(inventory);
    }

    @Override
    public List<InventoryResponse> findByItemIdOrName(CommanParam id) {
        List<Inventory> inventory = inventoryRepository.findByItemIdOrItemName(id.getId(), id.getName());
        if (inventory.isEmpty()) {
            throw new InventoryNotFoundException("Inventory not found , invalid id ");
        } else {
            return inventoryMapper.mapToInventoryResponse(inventory);
        }
    }

    @Override
    public InventoryResponse deleteByItemId(InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.findById(inventoryRequest.getBranchAndInventoryId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found , invalid id "));

        inventoryRepository.deleteById(inventory.getItemId());
        return inventoryMapper.mapToInventoryResponse(inventory);
    }


    @Override
    public List<InventoryResponse> findByAll() {
        List<Inventory> inventories = inventoryRepository.findAll();

        if (inventories.isEmpty()) {
            throw new InventoryNotFoundException("No Inventories Not Found");
        } else {
            return inventoryMapper.mapToInventoryResponse(inventories);
        }
    }

    @Override
    public List<String> fetchAllCategories() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream()
                .map(Inventory::getCategories)
                .distinct()
                .toList();
    }

    @Override
    public List<StockValueResponse> getStockValueList() {
        List<Inventory> inventories = inventoryRepository.findAll(); // Or fetchAllInventoryForStockValue()
//new
        if (inventories.isEmpty()) {

            throw new InventoryNotFoundException("No inventories found for stock value calculation");
        }

        List<StockValueResponse> stockValueResponses = new ArrayList<>();

        for (Inventory inventory : inventories) {
            StockValueResponse response = new StockValueResponse();
            response.setItemName(inventory.getItemName());

            double price = inventory.getItemCost();
            double quantity = inventory.getItemQuantity();

            response.setStockValue(quantity * price);
            stockValueResponses.add(response);
        }

        return stockValueResponses;
    }

    @Override
    public List<InventoryResponse> getInventoryByBranchId(CommanParam param){
        List<Inventory> inventories = inventoryRepository.findByBranch_BranchId(param.getId());

        if(inventories.isEmpty()){
            throw new InventoryNotFoundException("No inventories found for branch id " + param.getId());
        }
        return inventoryMapper.mapToInventoryResponse(inventories);
    }

    @Override
    public List<InventoryResponse> getLowStockItems() {
        List<Inventory> inventories = inventoryRepository.findAll();
        List<Inventory> lowStockItems = inventories.stream()
                .filter(item -> item.getItemQuantity() < item.getLowStockThreshold())
                .toList();

        if (lowStockItems.isEmpty()) {
            throw new InventoryNotFoundException("No Low Stock Items Found");
        }

        return inventoryMapper.mapToInventoryResponse(lowStockItems);
    }


    @Override
    public  ResultDto<InventoryAndBranchProjection>  getInventoryDetails(FilterRequest filterRequest){
        log.info("Into [InventoryServiceImpl] [getInventoryDetails] ");

        log.info("[InventoryServiceImpl] [getInventoryDetails] :: Request :: {} " ,
                ObjectMapperUtils.writeValueAsString(filterRequest));

        ResultDto<InventoryAndBranchProjection>  inventoryAndBranchProjections = new ResultDto<>();

        try{
            inventoryAndBranchProjections = inventoryCustomRepository.getInventoryDetails(filterRequest);
        }catch (Exception exception){
            log.error("Error [InventoryServiceImpl] [getInventoryDetails] :: {} {} " , exception.getMessage() , exception);
        }

        log.info("Exit [InventoryServiceImpl] [getInventoryDetails] ");

        return inventoryAndBranchProjections;
    }

    public boolean findById(long inventoryId){
        log.info("Into [InventoryServiceImpl] [findById] ");

        log.info("[InventoryServiceImpl] [findById] :: id {} " , inventoryId);

        boolean isExits = inventoryRepository.findByItemId(inventoryId);

        log.info("Exit [InventoryServiceImpl] [findById] ");

        return isExits;
    }
}
