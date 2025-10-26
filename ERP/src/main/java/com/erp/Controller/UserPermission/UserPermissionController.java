package com.erp.Controller.UserPermission;

import com.erp.Service.UserPermission.UserPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/permission")
@Slf4j
public class UserPermissionController {

    private final UserPermissionService userPermissionService;

    @DeleteMapping
    public ResponseEntity<?> deleteUserPermission(
            @RequestBody List<Long> userPermissionIds) {
        userPermissionService.deleteUserPermissionByIds(userPermissionIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
