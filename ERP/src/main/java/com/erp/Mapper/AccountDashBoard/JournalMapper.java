package com.erp.Mapper.AccountDashBoard;

import com.erp.Dto.JournalDTO;
import com.erp.Model.Journal;
import java.util.ArrayList;
import java.util.List;

public class JournalMapper {

    public static JournalDTO toDto(Journal entity) {
        JournalDTO dto = new JournalDTO();
        dto.setJournalId(entity.getJournalId());
        dto.setDate(entity.getDate());
        dto.setVoucherId(entity.getVoucherId());
        dto.setReferenceType(entity.getReferenceType());
        dto.setReferenceId(entity.getReferenceId());
        dto.setStatus(entity.getStatus());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static Journal toEntity(JournalDTO dto) {
        Journal entity = new Journal();
//        entity.setJournalId(dto.getJournalId());
        entity.setDate(dto.getDate());
        entity.setVoucherId(dto.getVoucherId());
        entity.setReferenceType(dto.getReferenceType());
        entity.setReferenceId(dto.getReferenceId());
        entity.setStatus(dto.getStatus());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static List<JournalDTO> toDtoList(List<Journal> entities) {
        List<JournalDTO> list = new ArrayList<>();
        for (Journal entity : entities) {
            list.add(toDto(entity));
        }
        return list;
    }
}