package com.erp.Service.UserPermission;

import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.RolesActionPermission;
import com.erp.Model.UserPermission;
import com.erp.Repository.UserPermission.UserPermissionRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.RoleActionPermission.RoleActionPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPermissionService {

    private final RoleActionPermissionService roleActionPermissionService;
    private final UserPermissionRepository userPermissionRepository;
    private final UserIdentity userIdentity;

    public void addUserPermisionBasedOnRole(long userId, String roleName){
        log.info("Into [UserPermissionService] [addUserPermisionBasedOnRole]");

        log.info("[UserPermissionService] [addUserPermisionBasedOnRole] :: roleName {} :: " , roleName);

        List<RolesActionPermission> rolesActionPermissions =
                roleActionPermissionService.getRoleActionPermissionByRoleName(roleName);

        saveUserPermissionData(rolesActionPermissions , userId);
        log.info("Exit [UserPermissionService] [addUserPermisionBasedOnRole]");
    }

    @Transactional
    public void deleteUserPermissionByIds(List<Long> userPermissionIds){
        log.info("Into [UserPermissionService] [deleteUserPermissionByIds]");

        log.info("[UserPermissionService] [deleteUserPermissionByIds] :: UserPermissionIds :: {} " , userPermissionIds);

        int noOfRowsUpdated =  userPermissionRepository.deactiveUserPermissionByIds(userPermissionIds);

        if(noOfRowsUpdated != userPermissionIds.size()){
            throw new ResourceNotFoundException("Some data is not able to deactivate please check it");
        }

        log.info("Exit [UserPermissionService] [deleteUserPermissionByIds]");


    }

    private void saveUserPermissionData(List<RolesActionPermission> rolesActionPermissions , long userId){
        log.info("Into [UserPermissionService] [saveUserPermissionData] ");

        List<UserPermission>  userPermissions = new ArrayList<>();

        for(RolesActionPermission rolesActionPermission : rolesActionPermissions){

            UserPermission userPermission = new UserPermission();

            Long createdByUserId = userIdentity.getCurrentUser().getId();

            userPermission.setUserId(userId);
            userPermission.setCreatedBy(createdByUserId);
            userPermission.setCreatedAt(LocalDateTime.now());
            userPermission.setActive(true);
            userPermission.setRoleActionPermission(rolesActionPermission.getId());

            userPermissions.add(userPermission);
        }

        userPermissionRepository.saveAll(userPermissions);

        log.info("Exit [UserPermissionService] [saveUserPermissionData]");
    }



}
