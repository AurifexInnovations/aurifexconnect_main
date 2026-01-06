package com.erp.Service.PurchaseOrder;

import com.erp.Dto.Request.PurchaseOrderRequest;
import com.erp.Dto.Response.PurchaseOrderItemDTO;
import com.erp.Dto.Response.PurchaseOrderResponse;
import com.erp.Enum.PurchaseOrderStatus;
import com.erp.Model.InventoryV2;
import com.erp.Model.PurchaseOrder;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import com.erp.Repository.PurchaseOrder.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository repo;
    private final InventoryRepositoryV2 inventoryRepo;

    @Override
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {

        InventoryV2 inv = inventoryRepo.findById(request.getInventory_id())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Double discount = request.getDiscount() != null ? request.getDiscount() : 0.0;

        PurchaseOrderItemDTO item = mapItem(inv, discount);

        PurchaseOrder po = new PurchaseOrder();
        po.setVendorId(request.getVendorId());
        po.setCustomerId(request.getCustomerId());
        po.setBranchId(request.getBranchId());
        po.setPoDate(request.getPoDate());
        po.setDeliveryDate(request.getDeliveryDate());
        po.setStatus(PurchaseOrderStatus.DRAFT);
        po.setInventoryId(inv.getItemId());

        po.setItemName(item.getItem_name());
        po.setQty(item.getQty());
        po.setMeasurementUnit(item.getMeasurement_unit());
        po.setMeasurementValue(item.getMeasurement_value());
        po.setPrice(item.getPrice());
        po.setDiscount(item.getDiscount());
        po.setTaxPercent(item.getTax_percent());
        po.setLineTotal(item.getLine_total());
        po.setTotalValue(item.getLine_total());

        repo.save(po);

        return buildResponse(po, item);
    }


    @Override
    public List<PurchaseOrderResponse> getAll() {
        return repo.findAll().stream()
                .map(po -> buildResponse(po, mapItemFromEntity(po)))
                .toList();
    }

    @Override
    public PurchaseOrderResponse getById(Long id) {
        PurchaseOrder po = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PO not found"));
        return buildResponse(po, mapItemFromEntity(po));
    }

    @Override
    public PurchaseOrderResponse update(Long id, PurchaseOrderRequest request) {

        PurchaseOrder po = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PO not found"));

        InventoryV2 inv = inventoryRepo.findById(request.getInventory_id())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        Double discount = request.getDiscount() != null ? request.getDiscount() : 0.0;

        PurchaseOrderItemDTO item = mapItem(inv, discount);

        po.setVendorId(request.getVendorId());
        po.setCustomerId(request.getCustomerId());
        po.setBranchId(request.getBranchId());
        po.setPoDate(request.getPoDate());
        po.setDeliveryDate(request.getDeliveryDate());
        po.setInventoryId(inv.getItemId());

        if (request.getStatus() != null) {
            po.setStatus(request.getStatus());
        }

        po.setItemName(item.getItem_name());
        po.setQty(item.getQty());
        po.setMeasurementUnit(item.getMeasurement_unit());
        po.setMeasurementValue(item.getMeasurement_value());
        po.setPrice(item.getPrice());
        po.setDiscount(item.getDiscount());
        po.setTaxPercent(item.getTax_percent());
        po.setLineTotal(item.getLine_total());
        po.setTotalValue(item.getLine_total());

        repo.save(po);

        return buildResponse(po, item);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private PurchaseOrderItemDTO mapItem(InventoryV2 inv, Double discount) {

        PurchaseOrderItemDTO item = new PurchaseOrderItemDTO();

        item.setInventory_id(inv.getItemId());
        item.setItem_name(inv.getItemName());
        item.setQty(inv.getStockQuantity());
        item.setMeasurement_unit(inv.getUnitType());
        item.setMeasurement_value(inv.getUnitTypeValue());
        item.setPrice(inv.getPurchasePrice());
        item.setDiscount(discount);

        Double base = inv.getStockQuantity() * inv.getPurchasePrice();
        Double afterDiscount = base - discount;

        Double tax = inv.getTax() != null ? inv.getTax().getTaxRate().doubleValue() : 0.0;
        item.setTax_percent(tax);

        Double taxAmount = afterDiscount * tax / 100;
        item.setLine_total(afterDiscount + taxAmount);

        return item;
    }

    private PurchaseOrderItemDTO mapItemFromEntity(PurchaseOrder po) {
        PurchaseOrderItemDTO item = new PurchaseOrderItemDTO();
        item.setInventory_id(po.getInventoryId());
        item.setItem_name(po.getItemName());
        item.setQty(po.getQty());
        item.setMeasurement_unit(po.getMeasurementUnit());
        item.setMeasurement_value(po.getMeasurementValue());
        item.setPrice(po.getPrice());
        item.setDiscount(po.getDiscount());
        item.setTax_percent(po.getTaxPercent());
        item.setLine_total(po.getLineTotal());
        return item;
    }

    private PurchaseOrderResponse buildResponse(PurchaseOrder po, PurchaseOrderItemDTO item) {

        PurchaseOrderResponse res = new PurchaseOrderResponse();
        res.setPo_id(po.getPoId());
        res.setVendor_id(po.getVendorId());
        res.setCustomer_id(po.getCustomerId());
        res.setBranch_id(po.getBranchId());
        res.setDate(po.getPoDate());
        res.setDelivery_date(po.getDeliveryDate());
        res.setStatus(po.getStatus());
        res.setTotal_value(po.getTotalValue());
        res.setCreated_at(po.getCreatedAt());
        res.setUpdated_at(po.getUpdatedAt());
        res.setItems(List.of(item));

        return res;
    }
}