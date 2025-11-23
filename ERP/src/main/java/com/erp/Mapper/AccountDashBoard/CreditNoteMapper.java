package com.erp.Mapper.AccountDashBoard;

import com.erp.Dto.CreditNoteDTO;
import com.erp.Model.CreditNote;

public class CreditNoteMapper {

    public static CreditNoteDTO toDto(CreditNote entity) {
        CreditNoteDTO dto = new CreditNoteDTO();
        dto.setCnId(entity.getCnId());
        dto.setInvoiceId(entity.getInvoiceId());
        dto.setDate(entity.getDate());
        dto.setAmount(entity.getAmount());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());
        dto.setCnNumber(entity.getCnNumber());
        return dto;
    }

    public static CreditNote toEntity(CreditNoteDTO dto) {
        CreditNote entity = new CreditNote();
//        entity.setCnId(dto.getCnId());
        entity.setInvoiceId(dto.getInvoiceId());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setReason(dto.getReason());
        entity.setStatus(dto.getStatus());
        entity.setCnNumber(dto.getCnNumber());
        return entity;
    }
}