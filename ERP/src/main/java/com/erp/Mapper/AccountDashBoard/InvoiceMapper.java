package com.erp.Mapper.AccountDashBoard;

import com.erp.Dto.InvoiceDTO;
import com.erp.Model.Invoice;
import java.util.ArrayList;
import java.util.List;

public class InvoiceMapper {

    public static InvoiceDTO toDto(Invoice entity) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(entity.getInvoiceId());
        dto.setSoId(entity.getSoId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setDate(entity.getDate());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setTaxId(entity.getTaxId());
        dto.setPendingStatus(entity.getPendingStatus());
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        return dto;
    }

    public static Invoice toEntity(InvoiceDTO dto) {
        Invoice entity = new Invoice();
//        entity.setInvoiceId(dto.getInvoiceId());
        entity.setSoId(dto.getSoId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setDate(dto.getDate());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setTaxId(dto.getTaxId());
        entity.setPendingStatus(dto.getPendingStatus());
        entity.setInvoiceNumber(dto.getInvoiceNumber());
        return entity;
    }

    public static List<InvoiceDTO> toDtoList(List<Invoice> entities) {
        List<InvoiceDTO> list = new ArrayList<>();
        for (Invoice entity : entities) {
            list.add(toDto(entity));
        }
        return list;
    }
}