package com.erp.Controller.Role;

import com.erp.Dto.Request.RoleListRequest;
import com.erp.Dto.Request.RoleRequest;
import com.erp.Dto.Response.RoleResponse;
import com.erp.Service.Role.RoleServices;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${app.base-url}")
public class RoleController {

    private final RoleServices roleService;

    @PostMapping("admin/roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT')")
    public ResponseEntity<ListResponseStructure<RoleResponse>> createRoles(@RequestBody @Valid RoleListRequest roleListRequest) {
        List<RoleResponse> roleResponses = roleService.createRoles(roleListRequest);
        return ResponseBuilder.success(HttpStatus.CREATED, "Roles created or retrieved successfully", roleResponses);
    }

    @GetMapping("admin/roles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ROOT')")
    public ResponseEntity<ListResponseStructure<RoleResponse>> getAllRoles() {
        List<RoleResponse> roleResponses = roleService.getAllRoles();
        return ResponseBuilder.success(HttpStatus.CREATED, "Roles created or retrieved successfully", roleResponses);
    }
}