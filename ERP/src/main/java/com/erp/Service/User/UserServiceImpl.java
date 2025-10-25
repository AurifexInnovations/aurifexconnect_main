package com.erp.Service.User;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.UserResponse;
import com.erp.Exception.SameEmail.SameEmailFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.User.UserMapper;
import com.erp.Model.*;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Role.RoleRepository;
import com.erp.Repository.RoleActionPermission.RoleActionPermissionRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Repository.UserPermission.UserPermissionRepository;
import com.erp.Security.util.UserIdentity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserIdentity userIdentity;
    private final static String DEFAULT_ROLE = "EMPLOYEE";


    RoleActionPermissionRepository roleActionPermissionRepository;

    UserPermissionRepository userPermissionRepository;

    @Override
   // @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        Admin currentAdmin = (Admin) userIdentity.getCurrentUser();
        String schemaName = currentAdmin.getSchemaName();
        TenantContext.setCurrentTenant(schemaName);

        try {
            if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                throw new SameEmailFoundException("Employee already exists with this email");
            }

            User user = userMapper.mapToUser(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRoles(new HashSet<>());
            user.setDesignation(userRequest.getDesignation());
            user.setCreatedByAdminId(currentAdmin.getId());
            user.setSchemaName(schemaName);
            user = userRepository.save(user);

            Set<Role> attachedRoles = new HashSet<>();
            for (RoleRequest roleRequest : userRequest.getRoles()) {
                String roleNameUpper = roleRequest.getRoleName().toUpperCase();
                Role existingRole = roleRepository.findByRoleName(roleNameUpper)
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setRoleName(roleNameUpper);
                            return roleRepository.save(newRole);
                        });
                attachedRoles.add(existingRole);
            }

            Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(DEFAULT_ROLE);
                        return roleRepository.save(newRole);
                    });
            attachedRoles.add(defaultRole);

            user.setRoles(attachedRoles);
            user = userRepository.save(user);

            // Step 4: Assign module–action permissions
            for (PermissionRequest permission : userRequest.getPermissions()) {
                Long moduleId = permission.getModuleId();

                for (Long actionId : permission.getActionId()) {
                    RolesActionPermission rap = RolesActionPermission.builder()
                            .roleId(attachedRoles.iterator().next().getRoleId())
                            .moduleId(moduleId)
                            .actionId(actionId)
                            .createdAt(LocalDateTime.now())
                            .active(true)
                            .build();
                    rap = roleActionPermissionRepository.save(rap);

                    UserPermission userPermission = UserPermission.builder()
                            .userId(user.getId())
                            .roleActionPermission(rap.getId())
                            .createdBy(currentAdmin.getId())
                            .createdAt(LocalDateTime.now())
                            .build();
                    userPermissionRepository.save(userPermission);
                }
            }

            return userMapper.mapToUserResponse(user);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating user", e);
        } finally {
            TenantContext.clear();
        }
    }


    @Override
    @Transactional()
    public List<UserResponse> getListOfUsers() {

        List<User> users = userRepository.findByIsActiveTrue() ;
        return userMapper.mapToListOfUserResponse(users);
    }

    @Override
    public UserResponse updateUserById(UserUpdateRequest userUpdateRequest) throws Exception{

        Admin currentAdmin = (Admin) userIdentity.getCurrentUser();

        User user = (User) userIdentity.getCurrentUser();

        if(user.getId() == userUpdateRequest.getId()){
            userMapper.mapTOUserEntity(userUpdateRequest,user);
        }else {
            throw new UserNotFoundException("With this user id: "+ userUpdateRequest.getId() + "user is currently not login !");
        }


        Set<Role> updatedRoles = new HashSet<>();
        for (RoleRequest roleReq : userUpdateRequest.getRoles()) {
            String roleNameUpper = roleReq.getRoleName().toUpperCase();
            Role role = roleRepository.findByRoleName(roleNameUpper)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(roleNameUpper);
                        return roleRepository.save(newRole);
                    });
            updatedRoles.add(role);
        }

        // Ensure default role exists
        Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(DEFAULT_ROLE);
                    return roleRepository.save(newRole);
                });
        updatedRoles.add(defaultRole);

        user.setRoles(updatedRoles);
        userRepository.save(user);

        updateUserModuleActionPermissions(user, updatedRoles, userUpdateRequest.getPermissions(), currentAdmin);
        return userMapper.mapToUserResponse(user);

    }


    private void updateUserModuleActionPermissions(User user, Set<Role> roles,
                                                   Set<PermissionRequest> permissionRequests,
                                                   Admin currentAdmin) {

        // Fetch existing user permissions
        List<UserPermission> existingPermissions = userPermissionRepository.findByUserId(user.getId());
        Set<Long> newPermissionIds = new HashSet<>();

        for (PermissionRequest permission : permissionRequests) {
            Long moduleId = permission.getModuleId();

            for (Long actionId : permission.getActionId()) {

                // Find or create RolesActionPermission
                RolesActionPermission rap = roleActionPermissionRepository
                        .findByRoleIdAndModuleIdAndActionId(
                                roles.iterator().next().getRoleId(), moduleId, actionId)
                        .orElseGet(() -> {
                            RolesActionPermission newRap = RolesActionPermission.builder()
                                    .roleId(roles.iterator().next().getRoleId())
                                    .moduleId(moduleId)
                                    .actionId(actionId)
                                    .createdAt(LocalDateTime.now())
                                    .active(true)
                                    .build();
                            return roleActionPermissionRepository.save(newRap);
                        });

                newPermissionIds.add(rap.getId());

                // If not already linked, create new user permission
                boolean exists = existingPermissions.stream()
                        .anyMatch(up -> up.getRoleActionPermission().equals(rap.getId()));

                if (!exists) {
                    UserPermission newUserPerm = UserPermission.builder()
                            .userId(user.getId())
                            .roleActionPermission(rap.getId())
                            .createdBy(currentAdmin.getId())
                            .createdAt(LocalDateTime.now())
                            .build();
                    userPermissionRepository.save(newUserPerm);
                }
            }
        }

        // Deactivate any old role-action permissions no longer used
        for (UserPermission oldPerm : existingPermissions) {
            if (!newPermissionIds.contains(oldPerm.getRoleActionPermission())) {
                RolesActionPermission rap = roleActionPermissionRepository
                        .findById(oldPerm.getRoleActionPermission())
                        .orElse(null);
                if (rap != null && rap.isActive()) {
                    rap.setActive(false);
                    roleActionPermissionRepository.save(rap);
                }
            }
        }
    }


    @Override
    public UserResponse deleteUserById(CommanParam commanParamId) {

        Admin currentUser = (Admin) userIdentity.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("No authenticated user found");
        }

        User user = userRepository.findById(commanParamId.getId())
                .orElseThrow(()-> new UserNotFoundException("User not found with this id: "+ commanParamId.getId()));

        user.setActive(false);
        userRepository.save(user);
        return userMapper.mapToUserResponse(user);

    }

    @Override
    public List<UserResponse> findByIdOrName(CommanParam commanParamIdOrName) {

        Admin currentUser = (Admin) userIdentity.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("No authenticated user found");
        }

        List<User> users = Collections.singletonList(userRepository.findByIdOrFirstNameAndIsActiveTrue(commanParamIdOrName.getId(), commanParamIdOrName.getName())
                .orElseThrow(() -> new UserNotFoundException(("User not found !"))));

        return userMapper.mapToListOfUserResponse(users);

    }
}