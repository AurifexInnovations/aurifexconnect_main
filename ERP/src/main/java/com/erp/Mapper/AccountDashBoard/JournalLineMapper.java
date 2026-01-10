package com.erp.Mapper.AccountDashBoard;

import com.erp.Dto.JournalLineDTO;
import com.erp.Model.JournalLine;
import java.util.ArrayList;
import java.util.List;

public class JournalLineMapper {

    public static JournalLineDTO toDto(JournalLine entity) {
        JournalLineDTO dto = new JournalLineDTO();
        dto.setLineId(entity.getLineId());
        dto.setJournalId(entity.getJournalId());
        dto.setCoaId(entity.getCoaId());
        dto.setDebit(entity.getDebit());
        dto.setCredit(entity.getCredit());
        dto.setLineDescription(entity.getLineDescription());
        return dto;
    }

    public static JournalLine toEntity(JournalLineDTO dto) {
        JournalLine entity = new JournalLine();
        entity.setLineId(dto.getLineId());
        entity.setJournalId(dto.getJournalId());
        entity.setCoaId(dto.getCoaId());
        entity.setDebit(dto.getDebit());
        entity.setCredit(dto.getCredit());
        entity.setLineDescription(dto.getLineDescription());
        return entity;
    }

    public static List<JournalLineDTO> toDtoList(List<JournalLine> entities) {
        List<JournalLineDTO> list = new ArrayList<>();
        for (JournalLine entity : entities) {
            list.add(toDto(entity));
        }
        return list;
    }
}