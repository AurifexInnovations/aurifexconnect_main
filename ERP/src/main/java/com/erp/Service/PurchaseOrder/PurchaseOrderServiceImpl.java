package com.erp.Service.PurchaseOrder;

import com.erp.CustomRepository.PurchaseOrderCustomRepository;
import com.erp.Dto.Request.CreatePORequestDTO;
import com.erp.Dto.Request.LineItemRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdatePORequestDTO;
import com.erp.Dto.Response.PurchaseOrderResponseDTO;
import com.erp.Dto.Response.LineItemResponseDTO;
import com.erp.Dto.Response.ResultDto;

import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.PurchaseOrder;
import com.erp.Model.PurchaseOrderItem;
import com.erp.Model.Vendor;

import com.erp.Repository.PurchaseOrder.PurchaseOrderItemRepository;
import com.erp.Repository.PurchaseOrder.PurchaseOrderRepository;
import com.erp.Repository.Vendor.VendorRepository;

import lombok.extern.slf4j.Slf4j;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository poRepo;

    @Autowired
    private PurchaseOrderItemRepository itemRepo;

    @Autowired
    private VendorRepository vendorRepo;

    @Autowired
    private PurchaseOrderCustomRepository purchaseOrderCustomRepository;

    @Override
    @Transactional
    public PurchaseOrderResponseDTO createPO(CreatePORequestDTO dto) {
        log.info("Into [PurchaseOrderServiceImpl] [createPO] :: vendorId = {}", dto.getVendorId());

        try {
            Vendor vendor = vendorRepo.findActiveVendorById(dto.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

            // Create PO
            PurchaseOrder po = new PurchaseOrder();
            po.setVendorId(dto.getVendorId());
            po.setDateIssued(dto.getDateIssued());
            po.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
            po.setShippingCost(dto.getShippingCost());
            po.setStatus("Draft");

             poRepo.save(po);

            // Create PO Number
            po.setPoNumber("PO-" + LocalDate.now().getYear() + "-" + String.format("%04d", po.getPoId()));
             PurchaseOrder purchaseOrder = poRepo.save(po);

            BigDecimal total = BigDecimal.ZERO;
            List<PurchaseOrderItem> itemEntities = new ArrayList<>();

            for (LineItemRequestDTO li : dto.getLineItems()) {

                BigDecimal discount = li.getDiscountPercent() == null ? BigDecimal.ZERO : li.getDiscountPercent();
                BigDecimal price = li.getUnitPrice() == null ? BigDecimal.ZERO : li.getUnitPrice();

                BigDecimal subtotal = price.multiply(li.getQuantity())
                        .subtract(
                                price.multiply(li.getQuantity())
                                        .multiply(discount.divide(BigDecimal.valueOf(100)))
                        );

                total = total.add(subtotal);

                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setPoId(purchaseOrder.getPoId());
                item.setProductId(li.getProductId());
                item.setQuantity(li.getQuantity());
                item.setUnitPrice(price);
                item.setDiscountPercent(discount);
                item.setSubtotal(subtotal);

                itemEntities.add(item);
            }

            itemRepo.saveAll(itemEntities);

            po.setTotalValue(total);
            poRepo.save(po);

            log.info("Exit [PurchaseOrderServiceImpl] [createPO]");
            return mapToResponse(po, vendor, itemEntities);

        } catch (Exception ex) {
            log.error("Error [PurchaseOrderServiceImpl] [createPO] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public ResultDto<PurchaseOrderResponseDTO> filterPurchaseOrders(FilterRequest filterRequest) {
        log.info("Into [PurchaseOrderServiceImpl] [filterPurchaseOrders]");

        try {
            ResultDto<PurchaseOrderResponseDTO> response =
                    purchaseOrderCustomRepository.filterPurchaseOrders(filterRequest);

            log.info("Exit [PurchaseOrderServiceImpl] [filterPurchaseOrders]");
            return response;

        } catch (Exception exception) {
            log.error("Error [PurchaseOrderServiceImpl] [filterPurchaseOrders] :: {} :: {}",
                    exception.getMessage(), exception);
            throw exception;
        }
    }

    @Override
    public PurchaseOrderResponseDTO getPODetail(Long id) {
        log.info("Into [PurchaseOrderServiceImpl] [getPODetail] :: poId = {}", id);

        try {
            PurchaseOrder po = poRepo.findActiveById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PO not found"));

            Vendor vendor = vendorRepo.findActiveVendorById(po.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

            List<PurchaseOrderItem> items = itemRepo.findByPoIdAndIsActiveTrue(po.getPoId());

            log.info("Exit [PurchaseOrderServiceImpl] [getPODetail]");
            return mapToResponse(po, vendor, items);

        } catch (Exception ex) {
            log.error("Error [PurchaseOrderServiceImpl] [getPODetail] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO updateStatus(Long id, String status) {
        log.info("Into [PurchaseOrderServiceImpl] [updateStatus] :: poId = {}", id);

        try {
            PurchaseOrder po = poRepo.findActiveById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PO not found"));

            po.setStatus(status);
            poRepo.save(po);

            Vendor vendor = vendorRepo.findActiveVendorById(po.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

            List<PurchaseOrderItem> items = itemRepo.findByPoIdAndIsActiveTrue(po.getPoId());

            log.info("Exit [PurchaseOrderServiceImpl] [updateStatus]");
            return mapToResponse(po, vendor, items);

        } catch (Exception ex) {
            log.error("Error [PurchaseOrderServiceImpl] [updateStatus] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO updatePO(Long id, UpdatePORequestDTO dto) {
        log.info("START :: [PurchaseOrderServiceImpl] [updatePO] :: poId = {}", id);

        try {


            Vendor vendor = vendorRepo.findActiveVendorById(dto.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));


            // 1. Fetch PO
            PurchaseOrder po = poRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found"));


            log.info("Fetched existing PO :: {}", po.getPoNumber());

            if (dto.getVendorId() != null) {
                po.setVendorId(dto.getVendorId());
                log.info("Updated vendorId = {}", dto.getVendorId());
            }

            if (dto.getDateIssued() != null) {
                po.setDateIssued(dto.getDateIssued());
                log.info("Updated dateIssued = {}", dto.getDateIssued());
            }

            if (dto.getExpectedDeliveryDate() != null) {
                po.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
                log.info("Updated expectedDeliveryDate = {}", dto.getExpectedDeliveryDate());
            }

            if (dto.getShippingCost() != null) {
                po.setShippingCost(dto.getShippingCost());
                log.info("Updated shippingCost = {}", dto.getShippingCost());
            }

            log.info("Deleting old line items for PO :: {}", id);
            itemRepo.deleteByPoId(id);

            log.info("Adding {} new line items", dto.getLineItems().size());

            List<PurchaseOrderItem> itemEntities = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (LineItemRequestDTO li : dto.getLineItems()) {

                BigDecimal discount = li.getDiscountPercent() == null ? BigDecimal.ZERO : li.getDiscountPercent();
                BigDecimal unitPrice = li.getUnitPrice() == null ? BigDecimal.ZERO : li.getUnitPrice();

                BigDecimal amount = unitPrice.multiply(li.getQuantity());
                BigDecimal discountValue = amount.multiply(discount.divide(BigDecimal.valueOf(100)));
                BigDecimal subtotal = amount.subtract(discountValue);

                total = total.add(subtotal);

                PurchaseOrderItem item = new PurchaseOrderItem();
                item.setPoId(id);
                item.setProductId(li.getProductId());
                item.setQuantity(li.getQuantity());
                item.setUnitPrice(unitPrice);
                item.setDiscountPercent(discount);
                item.setSubtotal(subtotal);

                itemEntities.add(item);

                log.info("Added lineItem :: productId={} qty={} price={} discount={} subtotal={}",
                        li.getProductId(), li.getQuantity(), unitPrice, discount, subtotal);
            }

            itemRepo.saveAll(itemEntities);

            po.setTotalValue(total);
            poRepo.save(po);

            log.info("END :: [updatePO] :: PO updated successfully :: total = {}", total);

            return mapToResponse(po, vendor, itemEntities);

        } catch (Exception ex) {
            log.error("ERROR :: [PurchaseOrderServiceImpl] [updatePO] :: {} :: ", ex.getMessage(), ex);
            throw ex;
        }
    }


    private PurchaseOrderResponseDTO mapToResponse(PurchaseOrder po,
                                                   Vendor vendor,
                                                   List<PurchaseOrderItem> items) {

        PurchaseOrderResponseDTO res = new PurchaseOrderResponseDTO();
        res.setPoId(po.getPoId());
        res.setVendorId(po.getVendorId());
        res.setVendorName(vendor.getVendorName());
        res.setPoNumber(po.getPoNumber());
        res.setStatus(po.getStatus());
        res.setDateIssued(po.getDateIssued());
        res.setExpectedDeliveryDate(po.getExpectedDeliveryDate());
        res.setShippingCost(po.getShippingCost());
        res.setTotalValue(po.getTotalValue());

        List<LineItemResponseDTO> lineItemResponses = new ArrayList<>();

        for (PurchaseOrderItem item : items) {
            LineItemResponseDTO li = new LineItemResponseDTO();
            li.setProductId(item.getProductId());
            li.setQuantity(item.getQuantity());
            li.setUnitPrice(item.getUnitPrice());
            li.setDiscountPercent(item.getDiscountPercent());
            li.setSubtotal(item.getSubtotal());
            lineItemResponses.add(li);
        }

        res.setLineItems(lineItemResponses);
        return res;
    }
}
