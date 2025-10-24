package com.erp.Controller.RoleActionPermission;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.RoleModleActionPermissionDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.RoleModleActionPermisisonResponse;
import com.erp.Service.RoleActionPermission.RoleActionPermissionService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/role/module/action/permission")
@RequiredArgsConstructor
public class RoleActionPermissionController {

    private final RoleActionPermissionService roleActionPermissionService;

    @PostMapping
    public ResponseEntity<ListResponseStructure<RoleModleActionPermisisonResponse>> addRoleModuleActionPermissions(@RequestBody List<RoleModleActionPermissionDto> roleModleActionPermissionDtos ) {
        List<RoleModleActionPermisisonResponse> roleModleActionPermisisonResponses  = roleActionPermissionService.addRoleModuleActionPermission(roleModleActionPermissionDtos);
        return ResponseBuilder.success(HttpStatus.CREATED, "role module action permission request submitted", roleModleActionPermisisonResponses);
    }

    @PutMapping
    public ResponseEntity<ListResponseStructure<RoleModleActionPermisisonResponse>> updateRoleModuleActionPermissions(@RequestBody List<RoleModleActionPermissionDto> roleModleActionPermissionDtos ) {
        List<RoleModleActionPermisisonResponse> roleModleActionPermisisonResponses  = roleActionPermissionService.updateRoleModuleActionPermission(roleModleActionPermissionDtos);
        return ResponseBuilder.success(HttpStatus.OK, "role module action permission updated Successfully", roleModleActionPermisisonResponses);
    }

    @PostMapping
    public ResponseEntity<ResultDto<RoleModleActionPermisisonResponse>> getRoleModulePermissions(@RequestBody FilterRequest filterRequest) {
        ResultDto<RoleModleActionPermisisonResponse>  roleModleActionPermisisonResponses  =
                roleActionPermissionService.getAllPermissionWithFilters(filterRequest);
        return ResponseEntity.ok(roleModleActionPermisisonResponses);
    }
}
