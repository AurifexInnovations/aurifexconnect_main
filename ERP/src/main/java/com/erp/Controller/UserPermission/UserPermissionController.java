package com.erp.Controller.UserPermission;

import com.erp.Dto.Response.RoleModleActionPermisisonResponse;
import com.erp.Service.UserPermission.UserPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/user/permission")
@Slf4j
public class UserPermissionController {

    private final UserPermissionService userPermissionService;

    @DeleteMapping
    public ResponseEntity<?> deleteUserPermission(@RequestParam(required = false) Long roleId,
                                                  @RequestParam(required = false) Long userId,
            @RequestBody List<Long> userPermissionIds) {
        userPermissionService.deleteUserPermissionByIds(roleId,userId,userPermissionIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<RoleModleActionPermisisonResponse>> getMyPermissions() {
        List<RoleModleActionPermisisonResponse> permissions = userPermissionService.getUserPermissions();
        return ResponseEntity.ok(permissions);
    }

}
