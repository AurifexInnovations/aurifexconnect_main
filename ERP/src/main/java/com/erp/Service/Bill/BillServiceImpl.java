package com.erp.Service.Bill;

import com.erp.CustomRepository.BillCustomRepository;
import com.erp.Dto.Request.BillLineItemDTO;
import com.erp.Dto.Request.BillRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateBillRequestDTO;
import com.erp.Dto.Response.BillResponseDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.Bill;
import com.erp.Model.BillItem;
import com.erp.Repository.Bill.BillItemRepository;
import com.erp.Repository.Bill.BillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillItemRepository billItemRepository;

    @Autowired
    private BillCustomRepository billCustomRepository;

    @Override
    @Transactional
    public BillResponseDTO createBill(BillRequestDTO dto) {
        log.info("Creating Bill: {}", dto);

        try {
            Bill bill = new Bill();
            bill.setPoId(dto.getPurchaseOrderId());
            bill.setVendorId(dto.getVendorId());
            bill.setBillDate(dto.getBillDate());
            bill.setDueDate(dto.getDueDate());
            bill.setTotalAmount(dto.getTotalAmount());
            bill.setBillNumber(dto.getBillNumber());
            bill.setStatus("Pending");

            billRepository.save(bill);
            log.info("Bill saved with ID {}", bill.getBillId());

            if (dto.getLineItems() != null && !dto.getLineItems().isEmpty()) {

                List<BillItem> items = dto.getLineItems().stream().map(itemDto -> {
                    BillItem item = new BillItem();
                    item.setBillId(bill.getBillId());
                    item.setProductId(itemDto.getProductId());
                    item.setQuantity(itemDto.getQuantity());
                    item.setUnitPrice(itemDto.getUnitPrice());
                    item.setTaxRate(itemDto.getTaxRate());
                    return item;
                }).collect(Collectors.toList());

                billItemRepository.saveAll(items);
                log.info("Line items saved for bill {}", bill.getBillId());
            }

            return getBillById(bill.getBillId());
        } catch (Exception e) {
            log.error("Error while creating bill. DTO: {}, Error: {}", dto, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    public BillResponseDTO getBillById(Long id) {
        try {
            log.info("Fetching Bill by ID {}", id);

            Bill bill = billRepository.findActiveBillById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

            List<BillItem> items = billItemRepository.findByBillIdAndIsActiveTrue(id);

            BillResponseDTO dto = mapToResponse(bill);

            List<BillLineItemDTO> itemDTOs = items.stream().map(item -> {
                BillLineItemDTO i = new BillLineItemDTO();
                i.setProductId(item.getProductId());
                i.setQuantity(item.getQuantity());
                i.setUnitPrice(item.getUnitPrice());
                i.setTaxRate(item.getTaxRate());
                return i;
            }).collect(Collectors.toList());

            dto.setLineItems(itemDTOs);

            return dto;
        } catch (Exception e) {
            log.error("Error fetching Bill ID {}. Message: {}", id, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    @Transactional
    public BillResponseDTO updateBill(Long id, UpdateBillRequestDTO dto) {
        log.info("Updating bill ID {} with DTO {}", id, dto);

        try {
            Bill bill = billRepository.findActiveBillById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

            if (dto.getBillDate() != null) bill.setBillDate(dto.getBillDate());
            if (dto.getDueDate() != null) bill.setDueDate(dto.getDueDate());
            if (dto.getTotalAmount() != null) bill.setTotalAmount(dto.getTotalAmount());
            if (dto.getStatus() != null) bill.setStatus(dto.getStatus());

            billRepository.save(bill);
            log.info("Bill {} updated successfully", id);

            // Delete old items
            if (dto.getLineItems() != null && !dto.getLineItems().isEmpty()) {

                List<BillItem> existingItems = billItemRepository.findByBillIdAndIsActiveTrue(id);
                if (!existingItems.isEmpty()) {
                    billItemRepository.deleteAll(existingItems);
                    log.info("Old line items deleted for bill {}", id);
                }

                List<BillItem> newItems = dto.getLineItems().stream().map(itemDto -> {
                    BillItem item = new BillItem();
                    item.setBillId(bill.getBillId());
                    item.setProductId(itemDto.getProductId());
                    item.setQuantity(itemDto.getQuantity());
                    item.setUnitPrice(itemDto.getUnitPrice());
                    item.setTaxRate(itemDto.getTaxRate());
                    return item;
                }).collect(Collectors.toList());

                billItemRepository.saveAll(newItems);
                log.info("New line items saved for bill {}", id);
            }

            return getBillById(id);
        } catch (Exception e) {
            log.error("Error updating Bill ID {}. Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    @Transactional
    public void updateBillStatus(Long id, String status) {
        log.info("Updating bill status. BillID={}, Status={}", id, status);

        try {
            Bill bill = billRepository.findActiveBillById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

            bill.setStatus(status);
            billRepository.save(bill);

            log.info("Status updated for Bill {} -> {}", id, status);
        } catch (Exception e) {
            log.error("Error updating bill status for ID {}. Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // --------------------------------------------------------
    // DEACTIVATE BILL
    // --------------------------------------------------------
    @Override
    @Transactional
    public void deactivateBill(Long id) {
        log.info("Deactivating Bill {}", id);

        try {
            Bill bill = billRepository.findActiveBillById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

            bill.setIsActive(false);
            billRepository.save(bill);

            List<BillItem> items = billItemRepository.findByBillIdAndIsActiveTrue(id);
            items.forEach(item -> item.setIsActive(false));
            billItemRepository.saveAll(items);

            log.info("Bill {} and its items deactivated", id);
        } catch (Exception e) {
            log.error("Error deactivating Bill {}. Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // --------------------------------------------------------
    // FILTER API
    // --------------------------------------------------------
    @Override
    public ResultDto<BillResponseDTO> getFilteredBills(FilterRequest filterRequest) {
        log.info("Filtering bills with {}", filterRequest);

        try {
            return billCustomRepository.getFilteredBills(filterRequest);
        } catch (Exception e) {
            log.error("Error filtering bills. Request: {}, Error: {}", filterRequest, e.getMessage(), e);
            throw e;
        }
    }

    private BillResponseDTO mapToResponse(Bill bill) {
        return mapToResponseWithTax(bill);
    }

    private BillResponseDTO mapToResponseWithTax(Bill bill) {
        BillResponseDTO dto = new BillResponseDTO();
        dto.setBillId(bill.getBillId());
        dto.setPoId(bill.getPoId());
        dto.setVendorId(bill.getVendorId());
        dto.setBillDate(bill.getBillDate());
        dto.setDueDate(bill.getDueDate());
        dto.setTotalAmount(bill.getTotalAmount());
        dto.setBillNumber(bill.getBillNumber());
        dto.setStatus(bill.getStatus());
        dto.setIsActive(bill.getIsActive());
        dto.setCreatedDate(bill.getCreatedDate());
        dto.setUpdatedDate(bill.getUpdatedDate());

        try {
            List<BillItem> items = billItemRepository.findByBillIdAndIsActiveTrue(bill.getBillId());

            BigDecimal taxApplied = items.stream()
                    .map(item -> item.getUnitPrice().multiply(item.getQuantity())
                            .multiply(item.getTaxRate().divide(BigDecimal.valueOf(100))))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // dto.setTaxApplied(taxApplied);

        } catch (Exception e) {
            log.error("Error calculating tax for bill {}. Error: {}", bill.getBillId(), e.getMessage(), e);
        }

        return dto;
    }
}
