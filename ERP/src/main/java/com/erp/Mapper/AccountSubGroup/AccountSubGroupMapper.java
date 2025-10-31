package com.erp.Mapper.AccountSubGroup;

import com.erp.Dto.Request.AccountSubGroupRequest;
import com.erp.Dto.Response.AccountSubGroupResponse;
import com.erp.Dto.Response.LedgerResponse;
import com.erp.Model.AccountSubGroup;
import com.erp.Model.Ledger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountSubGroupMapper {

    @Mapping(target = "groupId", source = "accountGroup.groupId")
    @Mapping(target = "groupName", source = "accountGroup.groupName")
    @Mapping(target = "groupCode", source = "accountGroup.groupCode")
    @Mapping(target = "ledgers", source = "ledgers")
    @Mapping(target = "totalLedgers", expression = "java(entity.getLedgers() != null ? entity.getLedgers().size() : 0)")
    AccountSubGroupResponse toResponse(AccountSubGroup entity);

    @Mapping(target = "subgroupId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "ledgers", ignore = true)
    @Mapping(target = "accountGroup", ignore = true)
    AccountSubGroup toEntity(AccountSubGroupRequest request);

    @Mapping(target = "ledgers", ignore = true)
    @Mapping(target = "accountGroup", ignore = true)
    void updateEntityFromRequest(AccountSubGroupRequest request, @MappingTarget AccountSubGroup entity);

    List<AccountSubGroupResponse> toResponseList(List<AccountSubGroup> entities);

    /**
     * Map Ledger to LedgerResponse for nested mapping
     */
    LedgerResponse mapLedgerToResponse(Ledger ledger);
}
