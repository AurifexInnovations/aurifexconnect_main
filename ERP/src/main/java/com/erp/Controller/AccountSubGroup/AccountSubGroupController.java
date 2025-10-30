package com.erp.Controller.AccountSubGroup;

import com.erp.Dto.Request.AccountSubGroupRequest;
import com.erp.Dto.Response.AccountSubGroupResponse;
import com.erp.Service.AccountSubGroup.AccountSubGroupService;
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
@RequestMapping("/api/v1/coa/subgroups")
@AllArgsConstructor
@Tag(name = "Chart of Accounts - Account SubGroups", description = "APIs for managing account subgroups in the chart of accounts")
public class AccountSubGroupController {

    private final AccountSubGroupService accountSubGroupService;

    @PostMapping
    @Operation(summary = "Create a new account subgroup", description = "Creates a new account subgroup in the chart of accounts")
    public ResponseEntity<AccountSubGroupResponse> createAccountSubGroup(
            @Valid @RequestBody AccountSubGroupRequest request) {
        AccountSubGroupResponse response = accountSubGroupService.createAccountSubGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{subgroupId}")
    @Operation(summary = "Update an account subgroup", description = "Updates an existing account subgroup")
    public ResponseEntity<AccountSubGroupResponse> updateAccountSubGroup(
            @Parameter(description = "Account subgroup ID") @PathVariable Long subgroupId,
            @Valid @RequestBody AccountSubGroupRequest request) {
        AccountSubGroupResponse response = accountSubGroupService.updateAccountSubGroup(subgroupId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{subgroupId}")
    @Operation(summary = "Get account subgroup by ID", description = "Retrieves account subgroup details by ID")
    public ResponseEntity<AccountSubGroupResponse> getAccountSubGroupById(
            @Parameter(description = "Account subgroup ID") @PathVariable Long subgroupId) {
        AccountSubGroupResponse response = accountSubGroupService.getAccountSubGroupById(subgroupId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{subgroupCode}")
    @Operation(summary = "Get account subgroup by code", description = "Retrieves account subgroup details by code")
    public ResponseEntity<AccountSubGroupResponse> getAccountSubGroupByCode(
            @Parameter(description = "Account subgroup code") @PathVariable String subgroupCode) {
        AccountSubGroupResponse response = accountSubGroupService.getAccountSubGroupByCode(subgroupCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all active account subgroups", description = "Retrieves all active account subgroups")
    public ResponseEntity<List<AccountSubGroupResponse>> getAllActiveAccountSubGroups() {
        List<AccountSubGroupResponse> response = accountSubGroupService.getAllActiveAccountSubGroups();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all account subgroups", description = "Retrieves all account subgroups including inactive ones")
    public ResponseEntity<List<AccountSubGroupResponse>> getAllAccountSubGroups() {
        List<AccountSubGroupResponse> response = accountSubGroupService.getAllAccountSubGroups();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "Get account subgroups by group ID", description = "Retrieves account subgroups for a specific group")
    public ResponseEntity<List<AccountSubGroupResponse>> getAccountSubGroupsByGroupId(
            @Parameter(description = "Account group ID") @PathVariable Long groupId) {
        List<AccountSubGroupResponse> response = accountSubGroupService.getAccountSubGroupsByGroupId(groupId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{subgroupId}/deactivate")
    @Operation(summary = "Deactivate account subgroup", description = "Soft deletes an account subgroup")
    public ResponseEntity<Void> deactivateAccountSubGroup(
            @Parameter(description = "Account subgroup ID") @PathVariable Long subgroupId) {
        accountSubGroupService.deactivateAccountSubGroup(subgroupId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{subgroupId}/activate")
    @Operation(summary = "Activate account subgroup", description = "Reactivates a deactivated account subgroup")
    public ResponseEntity<Void> activateAccountSubGroup(
            @Parameter(description = "Account subgroup ID") @PathVariable Long subgroupId) {
        accountSubGroupService.activateAccountSubGroup(subgroupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists/name/{subgroupName}/group/{groupId}")
    @Operation(summary = "Check if subgroup name exists", description = "Checks if an account subgroup name already exists in a group")
    public ResponseEntity<Boolean> isSubgroupNameExists(
            @Parameter(description = "Account subgroup name") @PathVariable String subgroupName,
            @Parameter(description = "Account group ID") @PathVariable Long groupId) {
        boolean exists = accountSubGroupService.isSubgroupNameExists(subgroupName, groupId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/code/{subgroupCode}")
    @Operation(summary = "Check if subgroup code exists", description = "Checks if an account subgroup code already exists")
    public ResponseEntity<Boolean> isSubgroupCodeExists(
            @Parameter(description = "Account subgroup code") @PathVariable String subgroupCode) {
        boolean exists = accountSubGroupService.isSubgroupCodeExists(subgroupCode);
        return ResponseEntity.ok(exists);
    }
}
