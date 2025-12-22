package com.erp.Mapper.receipt;



import com.erp.Dto.Request.ReceiptRequestDto;
import com.erp.Dto.Response.ReceiptResponseDto;
import com.erp.Model.Receipt;

import java.time.LocalDateTime;

public class ReceiptMapper {

    public static Receipt toEntity(ReceiptRequestDto dto) {
        Receipt receipt = new Receipt();
        receipt.setPaymentId(dto.getPaymentId());
        receipt.setInvoiceId(dto.getInvoiceId());
        receipt.setCustomerId(dto.getCustomerId());
        receipt.setReceiptNumber(dto.getReceiptNumber());
        receipt.setAmountReceived(dto.getAmountReceived());
        receipt.setPaymentMethod(dto.getPaymentMethod());
        receipt.setNotes(dto.getNotes());
        receipt.setReceiptDate(LocalDateTime.now());
        return receipt;
    }

    public static ReceiptResponseDto toDto(Receipt receipt) {
        ReceiptResponseDto dto = new ReceiptResponseDto();
        dto.setId(receipt.getId());
        dto.setPaymentId(receipt.getPaymentId());
        dto.setInvoiceId(receipt.getInvoiceId());
        dto.setCustomerId(receipt.getCustomerId());
        dto.setReceiptNumber(receipt.getReceiptNumber());
        dto.setReceiptDate(receipt.getReceiptDate());
        dto.setAmountReceived(receipt.getAmountReceived());
        dto.setPaymentMethod(receipt.getPaymentMethod());
        dto.setNotes(receipt.getNotes());
        dto.setCreatedAt(receipt.getCreatedAt());
        return dto;
    }
}
