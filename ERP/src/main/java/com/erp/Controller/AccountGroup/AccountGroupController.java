package com.erp.Controller.AccountGroup;

import com.erp.Dto.Request.AccountGroupRequest;
import com.erp.Dto.Response.AccountGroupResponse;
import com.erp.Enum.GroupType;
import com.erp.Service.AccountGroup.AccountGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coa/groups")
@AllArgsConstructor
@Tag(name = "Chart of Accounts - Account Groups", description = "APIs for managing account groups in the chart of accounts")
public class AccountGroupController {

    private final AccountGroupService accountGroupService;

    @PostMapping
    @Operation(summary = "Create a new account group", description = "Creates a new account group in the chart of accounts")
    public ResponseEntity<AccountGroupResponse> createAccountGroup(
            @Valid @RequestBody AccountGroupRequest request) {
        AccountGroupResponse response = accountGroupService.createAccountGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{groupId}")
    @Operation(summary = "Update an account group", description = "Updates an existing account group")
    public ResponseEntity<AccountGroupResponse> updateAccountGroup(
            @Parameter(description = "Account group ID") @PathVariable Long groupId,
            @Valid @RequestBody AccountGroupRequest request) {
        AccountGroupResponse response = accountGroupService.updateAccountGroup(groupId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "Get account group by ID", description = "Retrieves account group details by ID")
    public ResponseEntity<AccountGroupResponse> getAccountGroupById(
            @Parameter(description = "Account group ID") @PathVariable Long groupId) {
        AccountGroupResponse response = accountGroupService.getAccountGroupById(groupId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{groupCode}")
    @Operation(summary = "Get account group by code", description = "Retrieves account group details by code")
    public ResponseEntity<AccountGroupResponse> getAccountGroupByCode(
            @Parameter(description = "Account group code") @PathVariable String groupCode) {
        AccountGroupResponse response = accountGroupService.getAccountGroupByCode(groupCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all active account groups", description = "Retrieves all active account groups")
    public ResponseEntity<List<AccountGroupResponse>> getAllActiveAccountGroups() {
        List<AccountGroupResponse> response = accountGroupService.getAllActiveAccountGroups();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all account groups", description = "Retrieves all account groups including inactive ones")
    public ResponseEntity<List<AccountGroupResponse>> getAllAccountGroups() {
        List<AccountGroupResponse> response = accountGroupService.getAllAccountGroups();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{groupType}")
    @Operation(summary = "Get account groups by type", description = "Retrieves account groups filtered by type")
    public ResponseEntity<List<AccountGroupResponse>> getAccountGroupsByType(
            @Parameter(description = "Account group type") @PathVariable GroupType groupType) {
        List<AccountGroupResponse> response = accountGroupService.getAccountGroupsByType(groupType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{groupId}/deactivate")
    @Operation(summary = "Deactivate account group", description = "Soft deletes an account group")
    public ResponseEntity<Void> deactivateAccountGroup(
            @Parameter(description = "Account group ID") @PathVariable Long groupId) {
        accountGroupService.deactivateAccountGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{groupId}/activate")
    @Operation(summary = "Activate account group", description = "Reactivates a deactivated account group")
    public ResponseEntity<Void> activateAccountGroup(
            @Parameter(description = "Account group ID") @PathVariable Long groupId) {
        accountGroupService.activateAccountGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists/name/{groupName}")
    @Operation(summary = "Check if group name exists", description = "Checks if an account group name already exists")
    public ResponseEntity<Boolean> isGroupNameExists(
            @Parameter(description = "Account group name") @PathVariable String groupName) {
        boolean exists = accountGroupService.isGroupNameExists(groupName);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/code/{groupCode}")
    @Operation(summary = "Check if group code exists", description = "Checks if an account group code already exists")
    public ResponseEntity<Boolean> isGroupCodeExists(
            @Parameter(description = "Account group code") @PathVariable String groupCode) {
        boolean exists = accountGroupService.isGroupCodeExists(groupCode);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/initialize")
    @Operation(summary = "Initialize standard chart of accounts", description = "Creates standard account groups for a new company")
    public ResponseEntity<Void> initializeStandardChartOfAccounts() {
        accountGroupService.initializeStandardChartOfAccounts();
        return ResponseEntity.ok().build();
    }
}
