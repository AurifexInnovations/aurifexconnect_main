package com.erp.Service.InventoryService;

import com.erp.Dto.Request.InventoryRequestV2;
import com.erp.Dto.Request.InventoryUpdateRequestV2;
import com.erp.Dto.Response.InventoryResponse;
import com.erp.Dto.Response.InventoryResponseV2;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.VarientDto;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Mapper.Inventory.InventoryMapper;
import com.erp.Model.Branch;
import com.erp.Model.InventoryV2;
import com.erp.Model.Tax;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import com.erp.Repository.Service.ServiceRepository;
import com.erp.Repository.Tax.TaxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class InventoryServiceImplV2 implements InventoryServiceV2 {

    private final BranchRepository branchRepository;
    private final TaxRepository taxRepository;
    private final InventoryRepositoryV2 inventoryRepositoryV2;
    private final InventoryMapper inventoryMapper;
    private final ServiceRepository serviceRepository;

    @Override
    public ResultDto<InventoryResponseV2> addInventory(InventoryRequestV2 request) {
        List<InventoryResponseV2> responseV2List = new ArrayList<>();

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found!!"));

        Tax tax = taxRepository.findById(request.getTaxId())
                .orElseThrow(() -> new TaxNotFoundException("Tax Not Found!!"));

        if (request.isRentable()) {
            InventoryV2 inventory = new InventoryV2();

            inventory.setItemName(request.getItemName());
            inventory.setItemDescription(request.getItemDescription());
            inventory.setLowStockThreshold(request.getLowStockThreshold());

            inventory.setBranch(branch);

            inventory.setBrandName(request.getBrandName());
            inventory.setProductCategories(request.getProductCategories());
            inventory.setHsnCode(request.getHsnCode());
            inventory.setSkuCode(request.getSkuCode());
            inventory.setEan(request.getEan());
            inventory.setReturnable(request.isReturnable());
            inventory.setProductStatus(request.getProductStatus());
            inventory.setActive(request.isActive());

            inventory.setTax(tax);

            inventory.setRentable(true);
            inventory.setDefaultRentalRate(request.getDefaultRentalRate());
            inventory.setDefaultDepositAmount(request.getDefaultDepositAmount());
            inventory.setInsuranceValue(request.getInsuranceValue());
            inventory.setRentalRateFrequency(request.getRentalRateFrequency());
            inventory.setRentalProductQuantity(request.getRentalProductQuantity());
            inventory.setRentalProductStatus(request.getRentalProductStatus());

            InventoryV2 saved = inventoryRepositoryV2.save(inventory);
            InventoryResponseV2 responseV2 = inventoryMapper.ToInventoryResponseV2(saved);
            responseV2.setBranchId(branch.getBranchId());
            responseV2.setTaxId(tax.getId());

            responseV2List.add(responseV2);

        } else {
            for (VarientDto varient : request.getVariants()) {
                InventoryV2 inventory = new InventoryV2();

                inventory.setItemName(request.getItemName());
                inventory.setItemDescription(request.getItemDescription());
                inventory.setLowStockThreshold(request.getLowStockThreshold());

                inventory.setBranch(branch);

                inventory.setBrandName(request.getBrandName());
                inventory.setProductCategories(request.getProductCategories());
                inventory.setHsnCode(request.getHsnCode());
                inventory.setSkuCode(request.getSkuCode());
                inventory.setEan(request.getEan());
                inventory.setReturnable(request.isReturnable());
                inventory.setProductStatus(request.getProductStatus());
                inventory.setActive(request.isActive());

                inventory.setTax(tax);

                inventory.setStockQuantity(varient.getStockQuantity());
                inventory.setSellingPriceType(varient.getSellingPriceType());
                inventory.setSellingPrice(varient.getSellingPrice());
                inventory.setPurchasePriceType(varient.getPurchasePriceType());
                inventory.setPurchasePrice(varient.getPurchasePrice());
                inventory.setUnitType(varient.getUnitType());
                inventory.setUnitTypeValue(varient.getUnitTypeValue());
                inventory.setMeasurement(varient.getMeasurement());
                inventory.setMeasurementType(varient.getMeasurementType());
                inventory.setExpiryDate(varient.getExpiryDate());
                inventory.setActive(true);

                inventory.setItemId(null);
                InventoryV2 saved = inventoryRepositoryV2.save(inventory);
                InventoryResponseV2 responseV2 = inventoryMapper.ToInventoryResponseV2(saved);
                responseV2.setBranchId(branch.getBranchId());
                responseV2.setTaxId(tax.getId());

                responseV2List.add(responseV2);
            }
        }

        ResultDto<InventoryResponseV2> resultDto = new ResultDto<>();
        resultDto.setResults(responseV2List);
        resultDto.setCount(responseV2List.size());
        return resultDto;
    }


    @Override
    public InventoryResponseV2 deleteInventory(long itemId) {
        InventoryV2 inventoryV2 = inventoryRepositoryV2.findById(itemId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found !!"));

        for (com.erp.Model.Service s : inventoryV2.getServices()) {
            s.getInventories().remove(inventoryV2);
        }

        inventoryV2.getServices().clear();

        serviceRepository.saveAll(inventoryV2.getServices());

        InventoryResponseV2 inventoryResponseV2 = inventoryMapper.ToInventoryResponseV2(inventoryV2);
        inventoryResponseV2.setTaxId(inventoryV2.getTax().getId());
        inventoryResponseV2.setBranchId(inventoryV2.getBranch().getBranchId());

        inventoryRepositoryV2.delete(inventoryV2);

        return inventoryResponseV2;
    }

    @Override
    public InventoryResponseV2 updateInventory(InventoryUpdateRequestV2 request) {
        InventoryV2 updated = inventoryRepositoryV2.findById(request.getItemId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found !!"));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found"));

        Tax tax = taxRepository.findById(request.getTaxId())
                .orElseThrow(() -> new TaxNotFoundException("Tax Not Found"));

        InventoryV2 newInventory = inventoryMapper.toInventoryV2(request);
        inventoryMapper.convertToEntityToUpdated(newInventory, updated);

        updated.setBranch(branch);
        updated.setTax(tax);

        inventoryRepositoryV2.save(updated);

        InventoryResponseV2 responseV2 = inventoryMapper.ToInventoryResponseV2(updated);
        responseV2.setBranchId(updated.getBranch().getBranchId());
        responseV2.setTaxId(updated.getTax().getId());
        return responseV2;
    }

    @Override
    public ResultDto<InventoryResponseV2> getAll() {
        List<InventoryV2> inventories = inventoryRepositoryV2.findAll();

        List<InventoryResponseV2> inventoryResponseV2s = new ArrayList<>();
        for(InventoryV2 response : inventories){
            InventoryResponseV2 inventoryResponseV2 = inventoryMapper.ToInventoryResponseV2(response);
            inventoryResponseV2.setBranchId(response.getBranch().getBranchId());
            inventoryResponseV2.setTaxId(response.getTax().getId());
            inventoryResponseV2s.add(inventoryResponseV2);
        }

        ResultDto<InventoryResponseV2> resultDto = new ResultDto<>();
        resultDto.setCount(inventoryResponseV2s.size());
        resultDto.setResults(inventoryResponseV2s);
        return resultDto;
    }
}
