package com.erp.Service.AccountSubGroup;

import com.erp.Dto.Request.AccountSubGroupRequest;
import com.erp.Dto.Response.AccountSubGroupResponse;
import com.erp.Exception.AccountGroup.AccountGroupNotFoundException;
import com.erp.Exception.AccountSubGroup.AccountSubGroupAlreadyExistsException;
import com.erp.Exception.AccountSubGroup.AccountSubGroupNotFoundException;
import com.erp.Mapper.AccountSubGroup.AccountSubGroupMapper;
import com.erp.Model.AccountGroup;
import com.erp.Model.AccountSubGroup;
import com.erp.Repository.AccountGroup.AccountGroupRepository;
import com.erp.Repository.AccountSubGroup.AccountSubGroupRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountSubGroupServiceImpl implements AccountSubGroupService {

    private final AccountSubGroupRepository accountSubGroupRepository;
    private final AccountGroupRepository accountGroupRepository;
    private final AccountSubGroupMapper accountSubGroupMapper;

    @Override
    public AccountSubGroupResponse createAccountSubGroup(AccountSubGroupRequest request) {
        log.info("Creating account subgroup: {}", request.getSubgroupName());

        // Validate parent group exists
        AccountGroup accountGroup = accountGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with ID: " + request.getGroupId()));

        // Validate uniqueness
        if (isSubgroupNameExists(request.getSubgroupName(), request.getGroupId())) {
            throw new AccountSubGroupAlreadyExistsException("Account subgroup with name '" + request.getSubgroupName() + "' already exists in this group");
        }

        if (isSubgroupCodeExists(request.getSubgroupCode())) {
            throw new AccountSubGroupAlreadyExistsException("Account subgroup with code '" + request.getSubgroupCode() + "' already exists");
        }

        // Set sort order if not provided
        if (request.getSortOrder() == null) {
            request.setSortOrder(accountSubGroupRepository.getNextSortOrderForGroup(accountGroup));
        }

        AccountSubGroup accountSubGroup = accountSubGroupMapper.toEntity(request);
        accountSubGroup.setAccountGroup(accountGroup);
        accountSubGroup = accountSubGroupRepository.save(accountSubGroup);

        log.info("Successfully created account subgroup with ID: {}", accountSubGroup.getSubgroupId());
        return accountSubGroupMapper.toResponse(accountSubGroup);
    }

    @Override
    public AccountSubGroupResponse updateAccountSubGroup(Long subgroupId, AccountSubGroupRequest request) {
        log.info("Updating account subgroup with ID: {}", subgroupId);

        AccountSubGroup existingSubGroup = accountSubGroupRepository.findById(subgroupId)
                .orElseThrow(() -> new AccountSubGroupNotFoundException("Account subgroup not found with ID: " + subgroupId));

        // Validate parent group exists
        AccountGroup accountGroup = accountGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with ID: " + request.getGroupId()));

        // Validate uniqueness for updates
        if (!existingSubGroup.getSubgroupName().equals(request.getSubgroupName()) && 
            isSubgroupNameExistsForUpdate(request.getSubgroupName(), request.getGroupId(), subgroupId)) {
            throw new AccountSubGroupAlreadyExistsException("Account subgroup with name '" + request.getSubgroupName() + "' already exists in this group");
        }

        if (!existingSubGroup.getSubgroupCode().equals(request.getSubgroupCode()) && 
            isSubgroupCodeExistsForUpdate(request.getSubgroupCode(), subgroupId)) {
            throw new AccountSubGroupAlreadyExistsException("Account subgroup with code '" + request.getSubgroupCode() + "' already exists");
        }

        accountSubGroupMapper.updateEntityFromRequest(request, existingSubGroup);
        existingSubGroup.setAccountGroup(accountGroup);
        existingSubGroup = accountSubGroupRepository.save(existingSubGroup);

        log.info("Successfully updated account subgroup with ID: {}", subgroupId);
        return accountSubGroupMapper.toResponse(existingSubGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountSubGroupResponse getAccountSubGroupById(Long subgroupId) {
        log.info("Fetching account subgroup by ID: {}", subgroupId);

        AccountSubGroup accountSubGroup = accountSubGroupRepository.findById(subgroupId)
                .orElseThrow(() -> new AccountSubGroupNotFoundException("Account subgroup not found with ID: " + subgroupId));

        return accountSubGroupMapper.toResponse(accountSubGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountSubGroupResponse getAccountSubGroupByCode(String subgroupCode) {
        log.info("Fetching account subgroup by code: {}", subgroupCode);

        AccountSubGroup accountSubGroup = accountSubGroupRepository.findBySubgroupCode(subgroupCode)
                .orElseThrow(() -> new AccountSubGroupNotFoundException("Account subgroup not found with code: " + subgroupCode));

        return accountSubGroupMapper.toResponse(accountSubGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountSubGroupResponse> getAllActiveAccountSubGroups() {
        log.info("Fetching all active account subgroups");

        List<AccountSubGroup> accountSubGroups = accountSubGroupRepository.findByIsActiveTrueOrderBySortOrderAsc();
        return accountSubGroupMapper.toResponseList(accountSubGroups);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountSubGroupResponse> getAccountSubGroupsByGroupId(Long groupId) {
        log.info("Fetching account subgroups by group ID: {}", groupId);

        AccountGroup accountGroup = accountGroupRepository.findById(groupId)
                .orElseThrow(() -> new AccountGroupNotFoundException("Account group not found with ID: " + groupId));

        List<AccountSubGroup> accountSubGroups = accountSubGroupRepository.findByAccountGroupAndIsActiveTrueOrderBySortOrderAsc(accountGroup);
        return accountSubGroupMapper.toResponseList(accountSubGroups);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountSubGroupResponse> getAllAccountSubGroups() {
        log.info("Fetching all account subgroups");

        List<AccountSubGroup> accountSubGroups = accountSubGroupRepository.findAll();
        return accountSubGroupMapper.toResponseList(accountSubGroups);
    }

    @Override
    public void deactivateAccountSubGroup(Long subgroupId) {
        log.info("Deactivating account subgroup with ID: {}", subgroupId);

        AccountSubGroup accountSubGroup = accountSubGroupRepository.findById(subgroupId)
                .orElseThrow(() -> new AccountSubGroupNotFoundException("Account subgroup not found with ID: " + subgroupId));

        accountSubGroup.setIsActive(false);
        accountSubGroupRepository.save(accountSubGroup);

        log.info("Successfully deactivated account subgroup with ID: {}", subgroupId);
    }

    @Override
    public void activateAccountSubGroup(Long subgroupId) {
        log.info("Activating account subgroup with ID: {}", subgroupId);

        AccountSubGroup accountSubGroup = accountSubGroupRepository.findById(subgroupId)
                .orElseThrow(() -> new AccountSubGroupNotFoundException("Account subgroup not found with ID: " + subgroupId));

        accountSubGroup.setIsActive(true);
        accountSubGroupRepository.save(accountSubGroup);

        log.info("Successfully activated account subgroup with ID: {}", subgroupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubgroupNameExists(String subgroupName, Long groupId) {
        AccountGroup accountGroup = accountGroupRepository.findById(groupId).orElse(null);
        if (accountGroup == null) return false;
        return accountSubGroupRepository.findBySubgroupNameAndAccountGroup(subgroupName, accountGroup).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubgroupCodeExists(String subgroupCode) {
        return accountSubGroupRepository.findBySubgroupCode(subgroupCode).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubgroupNameExistsForUpdate(String subgroupName, Long groupId, Long excludeId) {
        AccountGroup accountGroup = accountGroupRepository.findById(groupId).orElse(null);
        if (accountGroup == null) return false;
        return accountSubGroupRepository.existsBySubgroupNameAndGroupAndNotId(subgroupName, accountGroup, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubgroupCodeExistsForUpdate(String subgroupCode, Long excludeId) {
        return accountSubGroupRepository.existsBySubgroupCodeAndNotId(subgroupCode, excludeId);
    }
}
