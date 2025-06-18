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
import com.erp.Repository.Role.RoleRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Security.util.UserIdentity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        // Check for duplicate email
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new SameEmailFoundException("Employee already exists with this email");
        }

        User user = userMapper.mapToUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoles(new HashSet<>()); // Clear any transient roles

        // Save user first to generate ID
        user = userRepository.save(user);

        // Process roles
        Set<Role> attachedRoles = new HashSet<>();
        for (RoleRequest roleRequest : userRequest.getRoles()) {
            String roleNameUpper = roleRequest.getRoleName().toUpperCase(); // Convert to uppercase

            Role existingRole = roleRepository.findByRoleName(roleNameUpper)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(roleNameUpper); // Save role in uppercase
                        return roleRepository.save(newRole);
                    });

            attachedRoles.add(existingRole);
        }

        // Add default role (e.g., "USER")
        Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(DEFAULT_ROLE);
                    return roleRepository.save(newRole);
                });
        attachedRoles.add(defaultRole);

        user.setRoles(attachedRoles);
        user = userRepository.save(user); // Save again to update roles

        return userMapper.mapToUserResponse(user); // Use MapStruct for mapping
    }

    @Override
    @Transactional()
    public List<UserResponse> getListOfUsers() {

        List<User> users = userRepository.findByIsActiveTrue() ;
        return userMapper.mapToListOfUserResponse(users);
    }

    @Override
    public UserResponse updateUserById(UserUpdateRequest userUpdateRequest) throws Exception{

        User user = (User) userIdentity.getCurrentUser();

        if(user.getId() == userUpdateRequest.getId()){
            userMapper.mapTOUserEntity(userUpdateRequest,user);
        }else {
            throw new UserNotFoundException("With this user id: "+ userUpdateRequest.getId() + "user is currently not login !");
        }
        userRepository.save(user);
        return userMapper.mapToUserResponse(user);

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