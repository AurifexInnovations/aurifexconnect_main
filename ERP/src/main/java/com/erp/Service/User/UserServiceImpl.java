package com.erp.Service.User;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.RoleRequest;
import com.erp.Dto.Request.UserRequest;
import com.erp.Dto.Request.UserUpdateRequest;
import com.erp.Dto.Response.UserResponse;
import com.erp.Exception.SameEmail.SameEmailFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.User.UserMapper;
import com.erp.Model.Admin;
import com.erp.Model.Role;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Role.RoleRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Security.util.UserIdentity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServices {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserIdentity userIdentity;
    private static final String DEFAULT_ROLE = "EMPLOYEE";

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        logger.info("Starting createUser for email: {}", userRequest.getEmail());
        Admin currentAdmin = (Admin) userIdentity.getCurrentUser();
        if (currentAdmin == null) {
            logger.error("No authenticated admin user found");
            throw new SecurityException("No authenticated user found");
        }
        String schemaName = currentAdmin.getSchemaName();
        TenantContext.setCurrentTenant(schemaName);
        try {
            boolean userExists = userRepository.findByEmail(userRequest.getEmail()).isPresent();
            if (userExists) {
                logger.warn("User creation failed - email already exists: {}", userRequest.getEmail());
                throw new SameEmailFoundException("Employee already exists with this email");
            }
            User user = userMapper.mapToUser(userRequest);
            // Defensive null check for modules
            if (user.getModules() == null) {
                logger.info("Modules not provided, setting empty module set");
                user.setModules(new HashSet<>());
            }
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRoles(new HashSet<>());
            user.setCreatedByAdminId(currentAdmin.getId());
            user.setSchemaName(schemaName);
            user = userRepository.save(user);
            logger.info("User saved initially with ID: {}", user.getId());
            Set<Role> attachedRoles = new HashSet<>();
            if (userRequest.getRoles() != null) {
                for (RoleRequest roleRequest : userRequest.getRoles()) {
                    if (roleRequest != null && roleRequest.getRoleName() != null) {
                        String roleNameUpper = roleRequest.getRoleName().toUpperCase();
                        Role existingRole = roleRepository.findByRoleName(roleNameUpper)
                                .orElseGet(() -> {
                                    Role newRole = new Role();
                                    newRole.setRoleName(roleNameUpper);
                                    return roleRepository.save(newRole);
                                });
                        attachedRoles.add(existingRole);
                    } else {
                        logger.warn("Null role or roleName found in request, skipping");
                    }
                }
            } else {
                logger.warn("No roles provided in the user request");
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
            logger.info("User saved with roles and modules, ID: {}", user.getId());
            return userMapper.mapToUserResponse(user);
        } catch (Exception ex) {
            logger.error("Error during user creation for email: {}", userRequest.getEmail(), ex);
            throw ex;
        } finally {
            TenantContext.clear();
            logger.info("Tenant context cleared post user creation");
        }
    }

    @Override
    @Transactional
    public List<UserResponse> getListOfUsers() {
        logger.info("Fetching list of active users");
        List<User> users = userRepository.findByIsActiveTrue();
        return userMapper.mapToListOfUserResponse(users);
    }

    @Override
    public UserResponse updateUserById(UserUpdateRequest userUpdateRequest) throws Exception {
        logger.info("Updating user with ID: {}", userUpdateRequest.getId());
        User user = (User) userIdentity.getCurrentUser();
        if (user == null) {
            logger.error("No authenticated user found for update");
            throw new SecurityException("No authenticated user found");
        }
        if (user.getId() == userUpdateRequest.getId()) {
            userMapper.mapTOUserEntity(userUpdateRequest, user);
            userRepository.save(user);
            logger.info("User updated successfully for ID {}", userUpdateRequest.getId());
            return userMapper.mapToUserResponse(user);
        } else {
            logger.error("User ID mismatch. Current logged in user ID: {}, update request ID: {}",
                    user.getId(), userUpdateRequest.getId());
            throw new UserNotFoundException("User ID mismatch or user not logged in");
        }
    }

    @Override
    public UserResponse deleteUserById(CommanParam commonParamId) {
        logger.info("Deleting (soft) user with ID: {}", commonParamId.getId());
        Admin currentUser = (Admin) userIdentity.getCurrentUser();
        if (currentUser == null) {
            logger.error("No authenticated admin found for delete operation");
            throw new SecurityException("No authenticated user found");
        }
        User user = userRepository.findById(commonParamId.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with this id: " + commonParamId.getId()));
        user.setActive(false);
        userRepository.save(user);
        logger.info("User set to inactive with ID: {}", user.getId());
        return userMapper.mapToUserResponse(user);
    }

    @Override
    public List<UserResponse> findByIdOrName(CommanParam commanParamIdOrName) {
        logger.info("Finding user by ID or name. ID: {}, Name: {}", commanParamIdOrName.getId(), commanParamIdOrName.getName());
        Admin currentUser = (Admin) userIdentity.getCurrentUser();
        if (currentUser == null) {
            logger.error("No authenticated user found for findByIdOrName");
            throw new SecurityException("No authenticated user found");
        }
        User user = userRepository.findByIdOrFirstNameAndIsActiveTrue(
                commanParamIdOrName.getId(),
                commanParamIdOrName.getName()
        ).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return userMapper.mapToListOfUserResponse(Collections.singletonList(user));
    }
}
