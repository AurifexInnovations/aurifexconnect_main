package com.erp.Service.AccountGroup;

import com.erp.Dto.Request.AccountGroupRequest;
import com.erp.Dto.Response.AccountGroupResponse;
import com.erp.Enum.GroupType;
import com.erp.Exception.AccountGroup.AccountGroupAlreadyExistsException;
import com.erp.Exception.AccountGroup.AccountGroupNotFoundException;
import com.erp.Mapper.AccountGroup.AccountGroupMapper;
import com.erp.Model.AccountGroup;
import com.erp.Repository.AccountGroup.AccountGroupRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountGroupServiceImpl implements AccountGroupService {

    private final AccountGroupRepository accountGroupRepository;
    private final AccountGroupMapper accountGroupMapper;

    @Override
    public AccountGroupResponse createAccountGroup(AccountGroupRequest request) {
        log.info("Creating account group: {}", request.getGroupName());

        // Validate uniqueness
        if (isGroupNameExists(request.getGroupName())) {
            throw new AccountGroupAlreadyExistsException("Account group with name '" + request.getGroupName() + "' already exists");
        }

        if (isGroupCodeExists(request.getGroupCode())) {
            throw new AccountGroupAlreadyExistsException("Account group with code '" + request.getGroupCode() + "' already exists");
        }

        // Set sort order if not provided
        if (request.getSortOrder() == null) {
            request.setSortOrder(accountGroupRepository.getNextSortOrder());
        }

        AccountGroup accountGroup = accountGroupMapper.toEntity(request);
        accountGroup = accountGroupRepository.save(accountGroup);

        log.info("Successfully created account group with ID: {}", accountGroup.getGroupId());
        return accountGroupMapper.toResponse(accountGroup);
    }

    @Override
    public AccountGroupResponse updateAccountGroup(Long groupId, AccountGroupRequest request) {
        log.info("Updating account group with ID: {}", groupId);

        AccountGroup existingGroup = accountGroupRepository.findById(groupId)
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with ID: " + groupId));

        // Validate uniqueness for updates
        if (!existingGroup.getGroupName().equals(request.getGroupName()) && 
            isGroupNameExistsForUpdate(request.getGroupName(), groupId)) {
            throw new AccountGroupAlreadyExistsException("Account group with name '" + request.getGroupName() + "' already exists");
        }

        if (!existingGroup.getGroupCode().equals(request.getGroupCode()) && 
            isGroupCodeExistsForUpdate(request.getGroupCode(), groupId)) {
            throw new AccountGroupAlreadyExistsException("Account group with code '" + request.getGroupCode() + "' already exists");
        }

        accountGroupMapper.updateEntityFromRequest(request, existingGroup);
        existingGroup = accountGroupRepository.save(existingGroup);

        log.info("Successfully updated account group with ID: {}", groupId);
        return accountGroupMapper.toResponse(existingGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountGroupResponse getAccountGroupById(Long groupId) {
        log.info("Fetching account group by ID: {}", groupId);

        AccountGroup accountGroup = accountGroupRepository.findById(groupId)
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with ID: " + groupId));

        return accountGroupMapper.toResponse(accountGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountGroupResponse getAccountGroupByCode(String groupCode) {
        log.info("Fetching account group by code: {}", groupCode);

        AccountGroup accountGroup = accountGroupRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with code: " + groupCode));

        return accountGroupMapper.toResponse(accountGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountGroupResponse> getAllActiveAccountGroups() {
        log.info("Fetching all active account groups");

        List<AccountGroup> accountGroups = accountGroupRepository.findByIsActiveTrueOrderBySortOrderAsc();
        return accountGroupMapper.toResponseList(accountGroups);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountGroupResponse> getAccountGroupsByType(GroupType groupType) {
        log.info("Fetching account groups by type: {}", groupType);

        List<AccountGroup> accountGroups = accountGroupRepository.findByGroupTypeAndIsActiveTrueOrderBySortOrderAsc(groupType);
        return accountGroupMapper.toResponseList(accountGroups);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountGroupResponse> getAllAccountGroups() {
        log.info("Fetching all account groups");

        List<AccountGroup> accountGroups = accountGroupRepository.findAll();
        return accountGroupMapper.toResponseList(accountGroups);
    }

    @Override
    public void deactivateAccountGroup(Long groupId) {
        log.info("Deactivating account group with ID: {}", groupId);

        AccountGroup accountGroup = accountGroupRepository.findById(groupId)
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with ID: " + groupId));

        accountGroup.setIsActive(false);
        accountGroupRepository.save(accountGroup);

        log.info("Successfully deactivated account group with ID: {}", groupId);
    }

    @Override
    public void activateAccountGroup(Long groupId) {
        log.info("Activating account group with ID: {}", groupId);

        AccountGroup accountGroup = accountGroupRepository.findById(groupId)
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with ID: " + groupId));

        accountGroup.setIsActive(true);
        accountGroupRepository.save(accountGroup);

        log.info("Successfully activated account group with ID: {}", groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGroupNameExists(String groupName) {
        return accountGroupRepository.findByGroupName(groupName).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGroupCodeExists(String groupCode) {
        return accountGroupRepository.findByGroupCode(groupCode).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGroupNameExistsForUpdate(String groupName, Long excludeId) {
        return accountGroupRepository.existsByGroupNameAndNotId(groupName, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGroupCodeExistsForUpdate(String groupCode, Long excludeId) {
        return accountGroupRepository.existsByGroupCodeAndNotId(groupCode, excludeId);
    }

    @Override
    public void initializeStandardChartOfAccounts() {
        log.info("Initializing standard chart of accounts");

        // Standard Asset Groups
        createStandardGroupIfNotExists("Fixed Assets", "FA", GroupType.ASSETS, "Tangible and intangible assets used in business operations");
        createStandardGroupIfNotExists("Current Assets", "CA", GroupType.ASSETS, "Assets expected to be converted to cash within one year");
        createStandardGroupIfNotExists("Investments", "INV", GroupType.ASSETS, "Long-term investments and securities");

        // Standard Liability Groups
        createStandardGroupIfNotExists("Current Liabilities", "CL", GroupType.LIABILITIES, "Debts due within one year");
        createStandardGroupIfNotExists("Long Term Liabilities", "LTL", GroupType.LIABILITIES, "Debts due after one year");

        // Standard Equity Groups
        createStandardGroupIfNotExists("Capital", "CAP", GroupType.EQUITY, "Owner's equity and capital contributions");
        createStandardGroupIfNotExists("Reserves & Surplus", "RES", GroupType.EQUITY, "Retained earnings and reserves");

        // Standard Income Groups
        createStandardGroupIfNotExists("Direct Income", "DI", GroupType.INCOME, "Revenue from primary business operations");
        createStandardGroupIfNotExists("Indirect Income", "II", GroupType.INCOME, "Revenue from secondary sources");

        // Standard Expense Groups
        createStandardGroupIfNotExists("Direct Expenses", "DE", GroupType.EXPENSES, "Costs directly related to revenue generation");
        createStandardGroupIfNotExists("Indirect Expenses", "IE", GroupType.EXPENSES, "Operating expenses not directly related to revenue");

        log.info("Standard chart of accounts initialization COMPLETED");
    }

    private void createStandardGroupIfNotExists(String name, String code, GroupType type, String description) {
        if (!isGroupNameExists(name)) {
            AccountGroupRequest request = AccountGroupRequest.builder()
                    .groupName(name)
                    .groupCode(code)
                    .groupType(type)
                    .description(description)
                    .isActive(true)
                    .build();
            createAccountGroup(request);
        }
    }
}
