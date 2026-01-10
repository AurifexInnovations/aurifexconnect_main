package com.erp.Mapper.AccountDashBoard;

import com.erp.Dto.ReceiptDTO;
import com.erp.Model.Receipt;
import java.util.ArrayList;
import java.util.List;

public class ReceiptMapper {

    public static ReceiptDTO toDto(Receipt entity) {
        ReceiptDTO dto = new ReceiptDTO();
        dto.setReceiptId(entity.getReceiptId());
        dto.setInvoiceId(entity.getInvoiceId());
        dto.setDate(entity.getDate());
        dto.setAmount(entity.getAmount());
        dto.setVoucherId(entity.getVoucherId());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    public static Receipt toEntity(ReceiptDTO dto) {
        Receipt entity = new Receipt();
        entity.setReceiptId(dto.getReceiptId());
        entity.setInvoiceId(dto.getInvoiceId());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setVoucherId(dto.getVoucherId());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    public static List<ReceiptDTO> toDtoList(List<Receipt> entities) {
        List<ReceiptDTO> list = new ArrayList<>();
        for (Receipt entity : entities) {
            list.add(toDto(entity));
        }
        return list;
    }
}