package com.erp.Service.AccountSubGroup;

import com.erp.Dto.Request.AccountSubGroupRequest;
import com.erp.Dto.Response.AccountSubGroupResponse;

import java.util.List;

public interface AccountSubGroupService {

    /**
     * Create a new account subgroup
     */
    AccountSubGroupResponse createAccountSubGroup(AccountSubGroupRequest request);

    /**
     * Update an existing account subgroup
     */
    AccountSubGroupResponse updateAccountSubGroup(Long subgroupId, AccountSubGroupRequest request);

    /**
     * Get account subgroup by ID
     */
    AccountSubGroupResponse getAccountSubGroupById(Long subgroupId);

    /**
     * Get account subgroup by code
     */
    AccountSubGroupResponse getAccountSubGroupByCode(String subgroupCode);

    /**
     * Get all active account subgroups
     */
    List<AccountSubGroupResponse> getAllActiveAccountSubGroups();

    /**
     * Get account subgroups by group ID
     */
    List<AccountSubGroupResponse> getAccountSubGroupsByGroupId(Long groupId);

    /**
     * Get all account subgroups (including inactive)
     */
    List<AccountSubGroupResponse> getAllAccountSubGroups();

    /**
     * Deactivate account subgroup (soft delete)
     */
    void deactivateAccountSubGroup(Long subgroupId);

    /**
     * Activate account subgroup
     */
    void activateAccountSubGroup(Long subgroupId);

    /**
     * Check if subgroup name exists within a group
     */
    boolean isSubgroupNameExists(String subgroupName, Long groupId);

    /**
     * Check if subgroup code exists
     */
    boolean isSubgroupCodeExists(String subgroupCode);

    /**
     * Check if subgroup name exists (excluding current record for updates)
     */
    boolean isSubgroupNameExistsForUpdate(String subgroupName, Long groupId, Long excludeId);

    /**
     * Check if subgroup code exists (excluding current record for updates)
     */
    boolean isSubgroupCodeExistsForUpdate(String subgroupCode, Long excludeId);
}
