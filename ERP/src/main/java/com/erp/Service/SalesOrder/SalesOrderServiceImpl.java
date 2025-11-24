package com.erp.Service.SalesOrder;

import com.erp.CustomRepository.SalesOrderCustomRepository;
import com.erp.Dto.Request.CreateSORequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.SalesOrderLineItemRequestDTO;
import com.erp.Dto.Request.UpdateSORequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SalesOrderLineItemResponseDTO;
import com.erp.Dto.Response.SalesOrderResponseDTO;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.SalesOrder;
import com.erp.Model.SalesOrderItem;
import com.erp.Repository.SalesOrder.SalesOrderItemRepository;
import com.erp.Repository.SalesOrder.SalesOrderRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SalesOrderServiceImpl implements SalesOrderService {

    @Autowired
    private SalesOrderRepository soRepo;

    @Autowired
    private SalesOrderItemRepository itemRepo;

    @Autowired
    private SalesOrderCustomRepository salesOrderCustomRepository;

    // ========================================================================
    // CREATE SALES ORDER
    // ========================================================================
    @Override
    @Transactional
    public SalesOrderResponseDTO createSO(CreateSORequestDTO dto) {

        log.info("START :: createSO :: customerId={}", dto.getCustomerId());

        // -------------------------------
        // 1. CREATE SALES ORDER
        // -------------------------------
        SalesOrder so = new SalesOrder();
        so.setCustomerId(dto.getCustomerId());
        so.setQuotationId(dto.getQuotationId());
        so.setOrderDate(dto.getOrderDate());
        so.setDeliveryTerms(dto.getDeliveryTerms());
        so.setSalesNotes(dto.getSalesNotes());
        so.setStatus("Draft");
        so.setUpdatedAt(LocalDateTime.now());

        soRepo.save(so);

        // -------------------------------
        // 2. CREATE LINE ITEMS
        // -------------------------------
        List<SalesOrderItem> items = buildItemEntities(dto.getLineItems(), so.getSoId());
        itemRepo.saveAll(items);

        // -------------------------------
        // 3. CALCULATE TOTAL
        // -------------------------------
        so.setTotalValue(calculateTotal(items));
        soRepo.save(so);

        log.info("END :: createSO");
        return mapToResponse(so, items);
    }

    // ========================================================================
    // FILTER SO
    // ========================================================================
    @Override
    public ResultDto<SalesOrderResponseDTO> filterSalesOrders(FilterRequest filterRequest) {
        return salesOrderCustomRepository.filterSalesOrders(filterRequest);
    }

    // ========================================================================
    // GET DETAIL
    // ========================================================================
    @Override
    public SalesOrderResponseDTO getSODetail(Long id) {

        SalesOrder so = soRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        List<SalesOrderItem> items = itemRepo.findBySoIdAndIsActiveTrue(id);

        return mapToResponse(so, items);
    }

    // ========================================================================
    // UPDATE STATUS
    // ========================================================================
    @Override
    @Transactional
    public SalesOrderResponseDTO updateStatus(Long id, String status) {

        SalesOrder so = soRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        so.setStatus(status);
        so.setUpdatedAt(LocalDateTime.now());
        soRepo.save(so);

        List<SalesOrderItem> items = itemRepo.findBySoIdAndIsActiveTrue(id);

        return mapToResponse(so, items);
    }

    // ========================================================================
    // UPDATE SALES ORDER
    // ========================================================================
    @Override
    @Transactional
    public SalesOrderResponseDTO updateSO(Long id, UpdateSORequestDTO dto) {

        SalesOrder so = soRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        // Update fields
        if (dto.getQuotationId() != null) so.setQuotationId(dto.getQuotationId());
        if (dto.getCustomerId() != null) so.setCustomerId(dto.getCustomerId());
        if (dto.getOrderDate() != null) so.setOrderDate(dto.getOrderDate());
        if (dto.getDeliveryTerms() != null) so.setDeliveryTerms(dto.getDeliveryTerms());
        if (dto.getSalesNotes() != null) so.setSalesNotes(dto.getSalesNotes());

        so.setUpdatedAt(LocalDateTime.now());

        // Delete old line items
        itemRepo.deleteBySoId(id);

        // Add new line items
        List<SalesOrderItem> items = buildItemEntities(dto.getLineItems(), id);
        itemRepo.saveAll(items);

        // Recalculate total
        so.setTotalValue(calculateTotal(items));
        soRepo.save(so);

        return mapToResponse(so, items);
    }

    // ========================================================================
    // HELPER: Build Line Items
    // ========================================================================
    private List<SalesOrderItem> buildItemEntities(List<SalesOrderLineItemRequestDTO> list, Long soId) {

        List<SalesOrderItem> items = new ArrayList<>();

        for (SalesOrderLineItemRequestDTO li : list) {

            BigDecimal qty = li.getQuantity() == null ? BigDecimal.ZERO : li.getQuantity();
            BigDecimal price = li.getUnitPrice() == null ? BigDecimal.ZERO : li.getUnitPrice();
            BigDecimal discount = li.getDiscountPercent() == null ? BigDecimal.ZERO : li.getDiscountPercent();

            BigDecimal subtotal = price.multiply(qty);
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                subtotal = subtotal.subtract(subtotal.multiply(discount).divide(BigDecimal.valueOf(100)));
            }

            SalesOrderItem item = new SalesOrderItem();
            item.setSoId(soId);
            item.setProductId(li.getProductId());
            item.setQuantity(qty);
            item.setUnitPrice(price);
            item.setDiscountPercent(discount);
            item.setSubtotal(subtotal);

            items.add(item);
        }

        return items;
    }

    // ========================================================================
    // HELPER: Total Calculation
    // ========================================================================
    private BigDecimal calculateTotal(List<SalesOrderItem> list) {
        return list.stream()
                .map(SalesOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ========================================================================
    // HELPER: Mapping to Response DTO
    // ========================================================================
    private SalesOrderResponseDTO mapToResponse(SalesOrder so, List<SalesOrderItem> items) {

        SalesOrderResponseDTO dto = new SalesOrderResponseDTO();

        dto.setSoId(so.getSoId());
        dto.setCustomerId(so.getCustomerId());
        dto.setQuoteId(so.getQuotationId());
        dto.setStatus(so.getStatus());
        dto.setOrderDate(so.getOrderDate());
        dto.setDeliveryTerms(so.getDeliveryTerms());
        dto.setSalesNotes(so.getSalesNotes());
        dto.setTotalValue(so.getTotalValue());
        dto.setCreatedBy(so.getCreatedBy());
        dto.setUpdatedAt(so.getUpdatedAt());

        List<SalesOrderLineItemResponseDTO> liList = new ArrayList<>();
        for (SalesOrderItem item : items) {

            SalesOrderLineItemResponseDTO li = new SalesOrderLineItemResponseDTO();
            li.setProductId(item.getProductId());
            li.setQuantity(item.getQuantity());
            li.setUnitPrice(item.getUnitPrice());
            li.setDiscountPercent(item.getDiscountPercent());
            li.setSubtotal(item.getSubtotal());

            liList.add(li);
        }

        dto.setLineItems(liList);
        return dto;
    }


    @Override
    @Transactional
    public void deactivateSO(Long soId) {

        SalesOrder so = soRepo.findById(soId)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        so.setIsActive(false);
        so.setUpdatedAt(LocalDateTime.now());
        soRepo.save(so);

        // Soft delete line items
        List<SalesOrderItem> items = itemRepo.findBySoIdAndIsActiveTrue(soId);
        for (SalesOrderItem item : items) {
            item.setIsActive(false);
        }
        itemRepo.saveAll(items);
    }

}
