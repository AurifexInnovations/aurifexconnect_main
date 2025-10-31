package com.erp.Service.AccountGroup;

import com.erp.Dto.Request.AccountGroupRequest;
import com.erp.Dto.Response.AccountGroupResponse;
import com.erp.Enum.GroupType;

import java.util.List;

public interface AccountGroupService {

    /**
     * Create a new account group
     */
    AccountGroupResponse createAccountGroup(AccountGroupRequest request);

    /**
     * Update an existing account group
     */
    AccountGroupResponse updateAccountGroup(Long groupId, AccountGroupRequest request);

    /**
     * Get account group by ID
     */
    AccountGroupResponse getAccountGroupById(Long groupId);

    /**
     * Get account group by code
     */
    AccountGroupResponse getAccountGroupByCode(String groupCode);

    /**
     * Get all active account groups
     */
    List<AccountGroupResponse> getAllActiveAccountGroups();

    /**
     * Get account groups by type
     */
    List<AccountGroupResponse> getAccountGroupsByType(GroupType groupType);

    /**
     * Get all account groups (including inactive)
     */
    List<AccountGroupResponse> getAllAccountGroups();

    /**
     * Deactivate account group (soft delete)
     */
    void deactivateAccountGroup(Long groupId);

    /**
     * Activate account group
     */
    void activateAccountGroup(Long groupId);

    /**
     * Check if group name exists
     */
    boolean isGroupNameExists(String groupName);

    /**
     * Check if group code exists
     */
    boolean isGroupCodeExists(String groupCode);

    /**
     * Check if group name exists (excluding current record for updates)
     */
    boolean isGroupNameExistsForUpdate(String groupName, Long excludeId);

    /**
     * Check if group code exists (excluding current record for updates)
     */
    boolean isGroupCodeExistsForUpdate(String groupCode, Long excludeId);

    /**
     * Initialize standard chart of accounts
     */
    void initializeStandardChartOfAccounts();
}
