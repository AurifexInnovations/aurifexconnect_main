package com.erp.Service.salesOrder;

import com.erp.Dto.Request.CalculationVar;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.QuotationProductRequestDto;
import com.erp.Dto.Request.SalesOrderRequestDto;
import com.erp.Dto.Response.SalesOrderResponseDto;
import com.erp.Enum.*;
import com.erp.Mapper.salesOrder.SalesOrderMapper;
import com.erp.Model.*;
import com.erp.Repository.Invoice.InvoiceMasterRepository;
import com.erp.Repository.payment.PaymentRepository;
import com.erp.Repository.salesOrder.SaledOrderProductMapperRepository;
import com.erp.Repository.salesOrder.SalesOrderRepository;
import com.erp.Repository.salesOrder.SalesOrderServiceMapperRepository;
import com.erp.Utility.AmountCalculationUtil;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final SaledOrderProductMapperRepository productRepo;
    private final SalesOrderServiceMapperRepository serviceRepo;
    private final InvoiceMasterRepository invoiceMasterRepository;
    private final AmountCalculationUtil amountCalculationUtil;
    private final PaymentRepository paymentRepository;

    @Override
    public SalesOrderResponseDto create(SalesOrderRequestDto dto) {

        log.info("Creating Sales Order");

        SalesOrder order = SalesOrderMapper.toEntity(dto);

        log.info("Discount amount: {}", dto.getDiscountPrice());
        BigDecimal discount =
                dto.getDiscountPrice() != null ? dto.getDiscountPrice() : BigDecimal.ZERO;

        getCalculation(dto, discount, order);

        SalesOrder savedOrder = salesOrderRepository.save(order);

    /* =========================
       SAVE LINE ITEMS (NO AMOUNTS)
       ========================= */
        if (dto.getSoType() == SalesOrderType.PRODUCT) {

            dto.getSalesOrderRequest().forEach(p -> {
                SaledOrderProductMapper m = new SaledOrderProductMapper();
                m.setProductId(p.getServiceOrProductId());
                m.setSaledOrderId(savedOrder.getSalesOrderNumber());
                m.setQuantity(BigDecimal.valueOf(p.getQuantity()));
                productRepo.save(m);
            });

        } else {

            dto.getSalesOrderRequest().forEach(p -> {
                SalesOrderServiceMapper m = new SalesOrderServiceMapper();
                m.setServiceId(p.getServiceOrProductId());
                m.setSalesOrderId(savedOrder.getSalesOrderNumber());
                serviceRepo.save(m);
            });
        }

        return SalesOrderMapper.toDto(savedOrder);
    }

    private void getCalculation(SalesOrderRequestDto dto, BigDecimal discount, SalesOrder order) {
        CalculationVar calc;

    /* =========================
       CALCULATION
       ========================= */
        if (dto.getSoType() == SalesOrderType.PRODUCT) {

            List<QuotationProductRequestDto> products =
                    dto.getSalesOrderRequest().stream()
                            .map(p -> QuotationProductRequestDto.builder()
                                    .productId(p.getServiceOrProductId())
                                    .quantity(p.getQuantity())
                                    .build())
                            .toList();

            calc = amountCalculationUtil.getCounting(
                    products,
                    discount.doubleValue()
            );

        } else { // SERVICE

            List<CommanParam> services =
                    dto.getSalesOrderRequest().stream()
                            .map(p -> new CommanParam(p.getServiceOrProductId()))
                            .toList();

            calc = amountCalculationUtil.serviceAmoCal(
                    services,
                    dto.getSqft().doubleValue(),
                    discount.doubleValue(),
                    ServiceCategory.valueOf(dto.getServiceCategory())
            );
        }

    /* =========================
       APPLY TOTALS TO SALES ORDER
       ========================= */
        order.setSubtotal(calc.getSubTotal());
        order.setTaxAmount(calc.getTaxAmount());
        order.setDiscountPrice(discount);
        order.setTotalAmount(calc.getSubTotal()); // before discount (optional)
        order.setGrandTotal(calc.getGrandTotal());
    }


//    private SalesOrderResponseDto getSalesOrderResponseDto(SalesOrderRequestDto dto, SalesOrder savedOrder) {
//
//
//        BigDecimal subTotal = BigDecimal.ZERO;
//        BigDecimal taxTotal = BigDecimal.ZERO;
//
//        if (dto.getSoType() == SalesOrderType.PRODUCT) {
//
//            List<SaledOrderProductMapper> items =
//                    productRepo.findBySaledOrderId(savedOrder.getSalesOrderNumber());
//
//            for (SaledOrderProductMapper item : items) {
//                subTotal = subTotal.add(item.getSubtotal());
//                taxTotal = taxTotal.add(item.getTaxAmount());
//            }
//
//        } else {
//
//            List<SalesOrderServiceMapper> items =
//                    serviceRepo.findBySalesOrderId(savedOrder.getSalesOrderNumber());
//
//            for (SalesOrderServiceMapper item : items) {
//                subTotal = subTotal.add(item.getSubtotal());
//                taxTotal = taxTotal.add(item.getTaxAmount());
//            }
//        }
//
//        BigDecimal totalAmount = subTotal.add(taxTotal);
//        BigDecimal discount = BigDecimal.ZERO;
//        BigDecimal grandTotal = totalAmount.subtract(discount);
//
//        Invoice invoice = new Invoice();
//        invoice.setCustomerId(dto.getCustomerId());
//        invoice.setQuotationId(dto.getQuotationId());
//        invoice.setSalesOrderId(savedOrder.getSalesOrderNumber());
//        invoice.setSqft(dto.getSqft());
//        invoice.setInvoiceNumber(NumberGeneratorUtil.generate("INV",invoiceMasterRepository.count()+1));
//        invoice.setSubtotal(subTotal);
//        invoice.setTaxAmount(taxTotal);
//        invoice.setTotalAmount(totalAmount);
//        invoice.setDiscountAmount(discount);
//        invoice.setGrandTotal(grandTotal);
//        invoice.setStatus(InvoiceStatus.DRAFT);
//        invoiceMasterRepository.save(invoice);
//        return null;
//    }

    @Override
    public SalesOrderResponseDto update(Long id, SalesOrderRequestDto dto) {
        log.info("Updating Sales Order {}", id);
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales order not found"));
        
        SalesOrder updated = SalesOrderMapper.toEntity(dto);
        updated.setSalesOrderNumber(order.getSalesOrderNumber());

        getCalculation(dto, updated.getDiscountPrice(),updated);

        SalesOrder savedOrder = salesOrderRepository.save(updated);

        if (updated.getStatus().equals(SalesOrderStatus.confirmed)){
            addOrUpdateInvoice(id, updated);
        }

        return SalesOrderMapper.toDto(salesOrderRepository.save(savedOrder));
    }

    private void addOrUpdatePayment(Invoice invoice){

        Payment payment = paymentRepository
                .findFirstByInvoiceId(invoice.getId())
                .orElseGet(Payment::new);

        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setInvoiceId(invoice.getId());
        payment.setCustomerId(invoice.getCustomerId());
        payment.setInvoiceAmount(invoice.getGrandTotal());
        if (payment.getId() == null) {
            payment.setCreatedAt(LocalDateTime.now());
        }
        paymentRepository.save(payment);
    }

    private void addOrUpdateInvoice(Long id, SalesOrder updated) {

        Invoice invoice = invoiceMasterRepository
                .findBySalesOrderId(id)
                .orElseGet(Invoice::new);

        boolean isNewInvoice = invoice.getId() == null;

        invoice.setCustomerId(updated.getCustomerId());
        invoice.setQuotationId(updated.getQuotationId());
        invoice.setSalesOrderId(id);
        invoice.setSqft(updated.getSqft());
        if (isNewInvoice) {
            invoice.setInvoiceNumber(
                    NumberGeneratorUtil.generate("INV", invoiceMasterRepository.count() + 1)
            );
            invoice.setStatus(InvoiceStatus.DRAFT);
        }
        invoice.setSubtotal(updated.getSubtotal());
        invoice.setTaxAmount(updated.getTaxAmount());
        invoice.setTotalAmount(updated.getTotalAmount());
        invoice.setGrandTotal(updated.getGrandTotal());
        invoice.setDiscountAmount(updated.getDiscountPrice());
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoiceMasterRepository.save(invoice);
        addOrUpdatePayment(invoice);

    }

    @Override
    public SalesOrderResponseDto getById(Long id) {
        return salesOrderRepository.findById(id)
                .map(SalesOrderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public List<SalesOrderResponseDto> getAll() {
        return salesOrderRepository.findAll()
                .stream()
                .map(SalesOrderMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting Sales Order {}", id);
        salesOrderRepository.deleteById(id);
    }
}
