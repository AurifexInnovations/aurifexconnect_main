package com.erp.Mapper.Ledger;

import com.erp.Dto.Request.LedgerRequest;
import com.erp.Dto.Response.LedgerResponse;
import com.erp.Model.Ledger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LedgerMapper {

    @Mapping(target = "groupId", source = "accountGroup.groupId")
    @Mapping(target = "groupName", source = "accountGroup.groupName")
    @Mapping(target = "groupCode", source = "accountGroup.groupCode")
    @Mapping(target = "subgroupId", source = "accountSubGroup.subgroupId")
    @Mapping(target = "subgroupName", source = "accountSubGroup.subgroupName")
    @Mapping(target = "subgroupCode", source = "accountSubGroup.subgroupCode")
    LedgerResponse mapToLedgerResponse(Ledger ledger);

    @Mapping(target = "ledgerId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "accountGroup", ignore = true)
    @Mapping(target = "accountSubGroup", ignore = true)
    @Mapping(target = "masters", ignore = true)
    @Mapping(target = "againstRefMaps", ignore = true)
    @Mapping(target = "currentBalance", ignore = true)
    Ledger maptoLedger(LedgerRequest request);

    @Mapping(target = "accountGroup", ignore = true)
    @Mapping(target = "accountSubGroup", ignore = true)
    @Mapping(target = "masters", ignore = true)
    @Mapping(target = "againstRefMaps", ignore = true)
    @Mapping(target = "currentBalance", ignore = true)
    void updateEntityFromRequest(LedgerRequest request, @MappingTarget Ledger entity);

    List<LedgerResponse> toResponseList(List<Ledger> entities);
}