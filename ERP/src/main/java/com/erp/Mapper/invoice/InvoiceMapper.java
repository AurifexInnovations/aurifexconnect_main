package com.erp.Mapper.invoice;

import com.erp.Dto.Request.InvoiceRequestDto;
import com.erp.Dto.Response.InvoiceResponseDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.CustomerDetails;
import com.erp.Model.Invoice;
import com.erp.Repository.costumer.CustomerDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




public class InvoiceMapper {


    // CREATE
    public static Invoice toEntity(InvoiceRequestDto dto) {
        Invoice invoice = new Invoice();
        updateEntity(invoice, dto);
        return invoice;
    }

    // UPDATE
    public static void updateEntity(Invoice invoice, InvoiceRequestDto dto) {
        invoice.setCustomerId(dto.getCustomerId());
        invoice.setSalesOrderId(dto.getSalesOrderId());
//        invoice.setQuotationId(dto.getQuotationId());
//        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setServiceCategory(dto.getServiceCategory());
        invoice.setSqft(dto.getSqft());
        invoice.setInvoiceIsFor(dto.getInvoiceIsFor());
        invoice.setStatus(dto.getStatus());
        invoice.setNotes(dto.getNotes());

        invoice.setSubtotal(dto.getSubtotal());
        invoice.setTaxAmount(dto.getTaxAmount());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setDiscountAmount(dto.getDiscountAmount());
        invoice.setGrandTotal(dto.getGrandTotal());
        invoice.setAmountPaid(dto.getAmountPaid());
        invoice.setBalanceAmount(dto.getBalanceAmount());
    }

    // RESPONSE
    public static InvoiceResponseDto toDto(Invoice invoice) {
        InvoiceResponseDto dto = new InvoiceResponseDto();
        dto.setId(invoice.getId());
        dto.setCustomerId(invoice.getCustomerId());
        dto.setSalesOrderId(invoice.getSalesOrderId());
        dto.setBranchId(invoice.getBranch().getBranchId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
//        dto.setQuotationId(invoice.getQuotationId());
//        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setServiceCategory(invoice.getServiceCategory());
        dto.setSqft(invoice.getSqft());
        dto.setInvoiceIsFor(invoice.getInvoiceIsFor());
        dto.setStatus(invoice.getStatus());
        dto.setPaymentStatus(invoice.getPaymentStatus());

        dto.setSubtotal(invoice.getSubtotal());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setGrandTotal(invoice.getGrandTotal());
        dto.setAmountPaid(invoice.getAmountPaid());
        dto.setBalanceAmount(invoice.getBalanceAmount());

        dto.setNotes(invoice.getNotes());
        dto.setCreatedAt(invoice.getCreatedAt());
        dto.setUpdatedAt(invoice.getUpdatedAt());


        return dto;
    }
}
