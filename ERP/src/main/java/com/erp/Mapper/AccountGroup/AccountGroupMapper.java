package com.erp.Mapper.AccountGroup;

import com.erp.Dto.Request.AccountGroupRequest;
import com.erp.Dto.Response.AccountGroupResponse;
import com.erp.Dto.Response.AccountSubGroupResponse;
import com.erp.Model.AccountGroup;
import com.erp.Model.AccountSubGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountGroupMapper {

    @Mapping(target = "accountSubGroups", source = "accountSubGroups")
    @Mapping(target = "totalSubGroups", expression = "java(entity.getAccountSubGroups() != null ? entity.getAccountSubGroups().size() : 0)")
    @Mapping(target = "totalLedgers", expression = "java(entity.getLedgers() != null ? entity.getLedgers().size() : 0)")
    AccountGroupResponse toResponse(AccountGroup entity);

    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Mapping(target = "accountSubGroups", ignore = true)
    @Mapping(target = "ledgers", ignore = true)
    AccountGroup toEntity(AccountGroupRequest request);

    @Mapping(target = "accountSubGroups", ignore = true)
    @Mapping(target = "ledgers", ignore = true)
    void updateEntityFromRequest(AccountGroupRequest request, @MappingTarget AccountGroup entity);

    List<AccountGroupResponse> toResponseList(List<AccountGroup> entities);

    /**
     * Map AccountSubGroup to AccountSubGroupResponse for nested mapping
     */
    AccountSubGroupResponse mapSubGroupToResponse(AccountSubGroup subgroup);
}
