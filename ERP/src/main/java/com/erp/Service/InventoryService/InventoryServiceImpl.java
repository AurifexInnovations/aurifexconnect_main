package com.erp.Service.InventoryService;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.InventoryRequest;
import com.erp.Dto.Response.InventoryResponse;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Mapper.Inventory.InventoryMapper;
import com.erp.Model.Branch;
import com.erp.Model.Inventory;
import com.erp.Model.Tax;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Tax.TaxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final BranchRepository branchRepository;
    private final TaxRepository taxRepository;

    @Override
    public InventoryResponse addItem(InventoryRequest inventoryRequest) {
        Branch branch = branchRepository.findById(inventoryRequest.getBranchAndInventoryId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found, Invalid Id"));

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
}
