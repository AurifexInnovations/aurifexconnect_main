package com.erp.Mapper.AccountDashBoard;

import com.erp.Dto.ChartOfAccountsDTO;
import com.erp.Model.ChartOfAccounts;
import java.util.ArrayList;
import java.util.List;

public class ChartOfAccountsMapper {

    public static ChartOfAccountsDTO toDto(ChartOfAccounts entity) {
        ChartOfAccountsDTO dto = new ChartOfAccountsDTO();
        dto.setCoaId(entity.getCoaId());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setAccountName(entity.getAccountName());
        dto.setAccountType(entity.getAccountType());
//        dto.setParentId(entity.getParentId());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public static ChartOfAccounts toEntity(ChartOfAccountsDTO dto) {
        ChartOfAccounts entity = new ChartOfAccounts();
//        entity.setCoaId(dto.getCoaId());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setAccountName(dto.getAccountName());
        entity.setAccountType(dto.getAccountType());
//        entity.setParentId(dto.getParentId());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }

    public static List<ChartOfAccountsDTO> toDtoList(List<ChartOfAccounts> entities) {
        List<ChartOfAccountsDTO> list = new ArrayList<>();
        for (ChartOfAccounts entity : entities) {
            list.add(toDto(entity));
        }
        return list;
    }
}