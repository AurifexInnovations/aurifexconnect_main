package com.erp.Service.RoleActionPermission;

import com.erp.CustomRepository.RoleActionPermissionCustomRepository;
import com.erp.Dto.Request.ActionDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ModuleDto;
import com.erp.Dto.Request.RoleModleActionPermissionDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.RoleModleActionPermisisonResponse;
import com.erp.Dto.Response.RoleResponse;
import com.erp.Exception.DBReltedException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.RolesActionPermission;
import com.erp.Repository.RoleActionPermission.RoleActionPermissionRepository;
import com.erp.Service.Action.ActionService;
import com.erp.Service.ModuleService.ModuleService;
import com.erp.Service.Role.RoleServices;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleActionPermissionService {

    private final RoleActionPermissionRepository roleActionPermissionRepository;
    private final ModuleService moduleService;
    private final ActionService actionService;
    private final RoleServices roleServices;

    private final RoleActionPermissionCustomRepository roleActionPermissionCustomRepository;

    public List<RoleModleActionPermisisonResponse> addRoleModuleActionPermission(List<RoleModleActionPermissionDto> roleModleActionPermissionDtos) {
        log.info("Into [RoleActionPermissionService] [addRoleModuleActionPermission]");

        log.info("[RoleActionPermissionService] [addRoleModuleActionPermission] :: Request :: {} ",
                ObjectMapperUtils.writeValueAsString(roleModleActionPermissionDtos));

        List<RolesActionPermission> rolesActionPermissions = new ArrayList<>();

        for (RoleModleActionPermissionDto roleModleActionPermissionDto : roleModleActionPermissionDtos){

            RolesActionPermission rolesActionPermission = new RolesActionPermission();

            RoleResponse roleResponse = roleServices.getRoleByRoleId(roleModleActionPermissionDto.getRoleId());
            ModuleDto moduleDto = moduleService.getModuleByModuleId(roleModleActionPermissionDto.getModuleId());
            ActionDto actionDto = actionService.getActionByActionId(roleModleActionPermissionDto.getActionId());

            rolesActionPermission.setRoleId(roleResponse.getRoleId());
            rolesActionPermission.setModuleId(moduleDto.getId());
            rolesActionPermission.setActionId(actionDto.getId());
            rolesActionPermission.setCreatedAt(LocalDateTime.now());
            rolesActionPermission.setActive(true);

            rolesActionPermissions.add(rolesActionPermission);
        }

        try {
            rolesActionPermissions = roleActionPermissionRepository.saveAll(rolesActionPermissions);
        } catch (DataIntegrityViolationException e) {
            throw new DBReltedException("Duplicate Entry not allowed ");
        }


        log.info("Exit [RoleActionPermissionService] [addRoleModuleActionPermission]");

        List<RoleModleActionPermisisonResponse> roleModleActionPermisisonResponses =
                createRoleModleActionPermisisonResponse(rolesActionPermissions);

        return roleModleActionPermisisonResponses;
    }


    public  List<RoleModleActionPermisisonResponse> updateRoleModuleActionPermission(List<RoleModleActionPermissionDto> roleModleActionPermissionDtos){
        log.info("Into [RoleActionPermissionService] [updateRoleModuleActionPermission]");

        log.info("[RoleActionPermissionService] [updateRoleModuleActionPermission] :: Request :: {} " ,
                ObjectMapperUtils.writeValueAsString(roleModleActionPermissionDtos));

        List<RolesActionPermission> rolesActionPermissions = new ArrayList<>();

        for (RoleModleActionPermissionDto roleModleActionPermissionDto :  roleModleActionPermissionDtos){

            RolesActionPermission rolesActionPermission =
                    validateAndGetRoleModuleActionPermissionPresentOrNot(roleModleActionPermissionDto.getRoleModulePermissionId());

            RoleResponse roleResponse = roleServices.getRoleByRoleId(roleModleActionPermissionDto.getRoleId());
            ModuleDto moduleDto = moduleService.getModuleByModuleId(roleModleActionPermissionDto.getModuleId());
            ActionDto actionDto = actionService.getActionByActionId(roleModleActionPermissionDto.getActionId());

            rolesActionPermission.setModuleId(moduleDto.getId());
            rolesActionPermission.setActionId(actionDto.getId());
            rolesActionPermission.setRoleId(roleResponse.getRoleId());

            rolesActionPermissions.add(rolesActionPermission);
        }

        try {
            rolesActionPermissions = roleActionPermissionRepository.saveAll(rolesActionPermissions);
        } catch (DataIntegrityViolationException e) {
            throw new DBReltedException("Duplicate Entry not allowed ");
        }

        List<RoleModleActionPermisisonResponse> roleModleActionPermisisonResponses =
                createRoleModleActionPermisisonResponse(rolesActionPermissions);

        log.info("Exit [RoleActionPermissionService] [updateRoleModuleActionPermission]");

        return roleModleActionPermisisonResponses;
    }

    public ResultDto<RoleModleActionPermisisonResponse> getAllPermissionWithFilters(FilterRequest filterRequest){
        log.info("Into [RoleActionPermissionService] [getRoleWiseModuleActionPermission]");

        ResultDto<RoleModleActionPermisisonResponse> roleModleActionPermisisonResponseResultDto =
                roleActionPermissionCustomRepository.getFilteredRoleModuleAction(filterRequest);

        log.info("Exit [RoleActionPermissionService] [getRoleWiseModuleActionPermission]");

        return roleModleActionPermisisonResponseResultDto;
    }

    public void deleteRoleModulePermission(List<Long> roleModulePermissionIds){
        log.info("Into [RoleActionPermissionService] [deleteRoleModulePermission]");



     int numberOfUpdatedRow =    roleActionPermissionRepository.deactivateRoleModulePermissionByIds(roleModulePermissionIds);

     if(numberOfUpdatedRow==0){
         throw new ResourceNotFoundException("Resource not found with provided ids "+ roleModulePermissionIds);
     }
        log.info("Exit [RoleActionPermissionService] [deleteRoleModulePermission]");
    }
    private List<RoleModleActionPermisisonResponse> createRoleModleActionPermisisonResponse(List<RolesActionPermission> rolesActionPermissions){

        log.info("Into [RoleActionPermissionService] [createRoleModleActionPermisisonResponse] ");

        List<RoleModleActionPermisisonResponse> roleModleActionPermisisonResponses = new ArrayList<>(rolesActionPermissions.size());

        for (RolesActionPermission rolesActionPermission : rolesActionPermissions){

            RoleModleActionPermisisonResponse roleModleActionPermisisonResponse = new RoleModleActionPermisisonResponse();
            roleModleActionPermisisonResponse.setRoleModulePermissionId(rolesActionPermission.getId());

            long roleId = rolesActionPermission.getRoleId();
            RoleResponse roleResponse =  roleServices.getRoleByRoleId(roleId);
            roleModleActionPermisisonResponse.setRole(roleResponse);

            long moduleId = rolesActionPermission.getModuleId();
            ModuleDto moduleDto = moduleService.getModuleByModuleId(moduleId);
            roleModleActionPermisisonResponse.setModule(moduleDto);

            long actionId = rolesActionPermission.getActionId();
            ActionDto actionDto = actionService.getActionByActionId(actionId);
            roleModleActionPermisisonResponse.setAction(actionDto);

            roleModleActionPermisisonResponses.add(roleModleActionPermisisonResponse);
        }

        log.info("Exit [RoleActionPermissionService] [createRoleModleActionPermisisonResponse] ");

        return roleModleActionPermisisonResponses;
    }
    private RolesActionPermission validateAndGetRoleModuleActionPermissionPresentOrNot(long roleModuleActionPermissionId){
        log.info("Into [RoleActionPermissionService] [validateRoleModuleActionPermissionPresentOrNot]");

        log.info("[RoleActionPermissionService] [validateRoleModuleActionPermissionPresentOrNot] :: RoleModuleActionPermissionId :: {} ",
                roleModuleActionPermissionId);

        RolesActionPermission rolesActionPermission =
                roleActionPermissionRepository.getById(roleModuleActionPermissionId);

        if(Objects.isNull(rolesActionPermission)){
            throw new ResourceNotFoundException("role action permission is not present please check this id " + roleModuleActionPermissionId);
        }

        log.info("Exit [RoleActionPermissionService] [validateRoleModuleActionPermissionPresentOrNot]");

        return rolesActionPermission;
    }

    public List<RolesActionPermission> getRoleActionPermissionByRoleName(String roleName){
        log.info("Into [RoleActionPermissionService] [getRoleActionPermissionByRoleName]");

        log.info("[RoleActionPermissionService] [getRoleActionPermissionByRoleName] :: RoleName :: {} " , roleName);

        List<RolesActionPermission> rolesActionPermissions =
                roleActionPermissionRepository.findByRoleName(roleName);

        log.info("Exit [RoleActionPermissionService] [getRoleActionPermissionByRoleName]");

        return  rolesActionPermissions;
    }
}
