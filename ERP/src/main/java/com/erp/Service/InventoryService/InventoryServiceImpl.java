package com.erp.Service.InventoryService;

import com.erp.CustomRepository.InventoryCustomRepository;
import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Dto.VarientDto;
import com.erp.Enum.Action;
import com.erp.Enum.ProductStatus;
import com.erp.Exception.Inventory_Exception.InventoryNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Inventory.InventoryMapper;
import com.erp.Mapper.Inventory.ProductMapper;
import com.erp.Model.Branch;
import com.erp.Model.Inventory;
import com.erp.Model.Tax;
import com.erp.Model.Varient;
import com.erp.Projection.InventoryAndBranchProjection;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Repository.Tax.TaxRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.Activity.ActivityService;
import com.erp.Service.Utility.FileService;
import com.erp.Service.Varients.VarientService;
import com.erp.Utility.ObjectMapperUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.TypedQuery;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;


import java.time.LocalDateTime;
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
    private final VarientService varientService;
    private final ProductMapper productMapper;

    private final FileService fileService;

    private final ActivityService activityService;

    private final UserIdentity userIdentity;

    @Autowired
    private EntityManager entityManager;

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
    public List<InventoryResponse> getInventoryByBranchId(CommanParam param) {
        List<Inventory> inventories = inventoryRepository.findByBranch_BranchId(param.getId());

        if (inventories.isEmpty()) {
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
    public ResultDto<InventoryAndBranchProjection> getInventoryDetails(FilterRequest filterRequest) {
        log.info("Into [InventoryServiceImpl] [getInventoryDetails] ");

        log.info("[InventoryServiceImpl] [getInventoryDetails] :: Request :: {} ",
                ObjectMapperUtils.writeValueAsString(filterRequest));

        ResultDto<InventoryAndBranchProjection> inventoryAndBranchProjections = new ResultDto<>();

        try {
            inventoryAndBranchProjections = inventoryCustomRepository.getInventoryDetails(filterRequest);
        } catch (Exception exception) {
            log.error("Error [InventoryServiceImpl] [getInventoryDetails] :: {} {} ", exception.getMessage(), exception);
        }

        log.info("Exit [InventoryServiceImpl] [getInventoryDetails] ");

        return inventoryAndBranchProjections;
    }

    @Override
    public ResultDto<InventoryAndBranchProjection> getInventoryDetails() {
        log.info("Into [InventoryServiceImpl] [getInventoryDetails] ");


        ResultDto<InventoryAndBranchProjection> inventoryAndBranchProjections = new ResultDto<>();

        try {
            inventoryAndBranchProjections = inventoryCustomRepository.getAllInventory();
        } catch (Exception exception) {
            log.error("Error [InventoryServiceImpl] [getInventoryDetails] :: {} {} ", exception.getMessage(), exception);
        }

        log.info("Exit [InventoryServiceImpl] [getInventoryDetails] ");

        return inventoryAndBranchProjections;
    }

    public boolean findById(long inventoryId) {
        log.info("Into [InventoryServiceImpl] [findById] ");

        log.info("[InventoryServiceImpl] [findById] :: id {} ", inventoryId);

        boolean isExits = inventoryRepository.findByItemId(inventoryId);

        log.info("Exit [InventoryServiceImpl] [findById] ");

        return isExits;
    }


    @Override
    @Transactional
    public Inventory createOrUpdateProduct(ProductRequest productRequest, MultipartFile[] files) {
        Long itemId = productRequest.getItemId();
        log.info("Add/Update product request received :: itemId={}, itemName={}", itemId, productRequest.getItemName());

        try {
            Inventory product;

            boolean isNewProduct = (itemId == null || !inventoryRepository.existsById(itemId));

            if (!isNewProduct) {
                log.info("Existing product found. Updating product with id={}", itemId);
                product = inventoryRepository.findById(itemId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id=" + itemId));

                productMapper.updateProductFromRequest(productRequest, product);

                ActivityDto activityDto=new ActivityDto();
                activityDto.setAction(Action.UPDATE_INVENTORY.toString());
                activityDto.setInventoryId(itemId);
                activityDto.setPerformedBy(userIdentity.getCurrentUsername());
                activityService.addActivity(itemId,activityDto);


            } else {
                log.info("No existing product found. Creating a new product for branchId={}", productRequest.getBranchId());

                product = productMapper.toEntity(productRequest);
                product.setCreatedAt(LocalDateTime.now());


                product = inventoryRepository.save(product);
                ActivityDto activityDto=new ActivityDto();
                activityDto.setAction(Action.ADD_INVENTORY.toString());
                activityDto.setInventoryId(product.getItemId());
                activityDto.setPerformedBy(userIdentity.getCurrentUsername());
                activityService.addActivity(product.getItemId(),activityDto);
            }

            product = inventoryRepository.save(product);
            log.info("Product saved successfully with id={}", product.getItemId());

            if (productRequest.getVarientList() != null && !productRequest.getVarientList().isEmpty()) {

                productRequest.getVarientList().forEach(dto -> dto.setItemId(itemId));

                if (isNewProduct) {

                    varientService.addAndUpdateVarients(productRequest.getVarientList());
                    log.info("Added new variants for productId={}", product.getItemId());
                } else {

                    varientService.updateVarients(productRequest.getVarientList());
                    log.info("Updated variants for productId={}", product.getItemId());
                }
            }

            if (files != null && files.length > 0) {
                fileService.uploadFiles(product.getItemId(), "INVENTORY", files);
            }

            return product;

        } catch (Exception e) {
            log.error("Error while creating/updating product for id={} :: {}", itemId, e.getMessage(), e);
            throw new ResourceNotFoundException("Failed to create or update product");
        }
    }



    @Transactional
    public void deleteInventoryByItemId(Long itemId) {
        log.info("Attempting to delete inventory with itemId={}", itemId);

        try {
            Optional<Inventory> exists = inventoryRepository.findByItemIdAndActiveTrue(itemId);

            if (exists.isEmpty()) {
                log.warn("Inventory not found for itemId={}", itemId);
                throw new ResourceNotFoundException("Inventory not found for itemId = " + itemId);
            }

            inventoryRepository.setInactiveByItemId(itemId);
            log.info("Successfully deleted Inventory with itemId={}", itemId);

            ActivityDto activityDto=new ActivityDto();
            activityDto.setAction(Action.DELETE_INVENTORY.toString());
            activityDto.setInventoryId(itemId);
            activityDto.setPerformedBy(userIdentity.getCurrentUsername());
            activityService.addActivity(itemId,activityDto);

        } catch (ResourceNotFoundException e) {// rethrow to handle in controller
        } catch (Exception e) {
            log.error("Error deleting Inventory with itemId={}", itemId, e);
            throw new RuntimeException("Error deleting inventory with itemId = " + itemId, e);
        }
    }


}
