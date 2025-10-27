package com.erp.Service.UserPermission;

import com.erp.Dto.Request.ActionDto;
import com.erp.Dto.Request.ModuleDto;
import com.erp.Dto.Response.RoleModleActionPermisisonResponse;
import com.erp.Dto.Response.RoleResponse;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.RolesActionPermission;
import com.erp.Model.UserPermission;
import com.erp.Projection.RoleModuleActionProjection;
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
import java.util.Objects;

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


    public List<RoleModleActionPermisisonResponse> getUserPermissions() {
        List<RoleModleActionPermisisonResponse> responseList = new ArrayList<>();

        try {
            Long userId = userIdentity.getCurrentUser().getId();
            log.info("Fetching permissions for userId: {}", userId);

            List<RoleModuleActionProjection> results = userPermissionRepository.findRoleModuleActionPermissionsByUserId(userId);
            log.info("Fetched {} permission records for userId: {}", results.size(), userId);

            responseList = results.stream()
                    .map(this::mapToResponse)
                    .filter(Objects::nonNull)
                    .toList();

            log.info("Mapped all permissions successfully for userId: {}", userId);
        } catch (Exception e) {
            log.error("Error while fetching or mapping user permissions: {}", e.getMessage(), e);
        }

        return responseList;
    }

    private RoleModleActionPermisisonResponse mapToResponse(RoleModuleActionProjection p) {
        if (p == null) return null;

        RoleModleActionPermisisonResponse response = new RoleModleActionPermisisonResponse();
        response.setUserPermissonId(p.getUserPermissionId());
        response.setRoleModulePermissionId(p.getRoleModulePermissionId());
        response.setRole(mapRole(p));
        response.setModule(mapModule(p));
        response.setAction(mapAction(p));

        return response;
    }

    private RoleResponse mapRole(RoleModuleActionProjection p) {
        RoleResponse role = new RoleResponse();
        role.setRoleId(p.getRoleId());
        role.setRoleName(p.getRoleName());
        return role;
    }

    private ModuleDto mapModule(RoleModuleActionProjection p) {
        ModuleDto module = new ModuleDto();
        module.setId(p.getModuleId());      // ✅ Changed
        module.setName(p.getModuleName());  // ✅ Changed
        return module;
    }

    private ActionDto mapAction(RoleModuleActionProjection p) {
        ActionDto action = new ActionDto();
        action.setId(p.getActionId());      // ✅ Changed
        action.setName(p.getActionName());
        action.setDescription(p.getDescription());// ✅ Changed
        return action;
    }


}
