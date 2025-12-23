package com.erp.Service.User;

import com.erp.CustomRepository.UserCustomRepository;
import com.erp.Dto.Request.*;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.FileUploadResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.UserResponse;
import com.erp.Exception.Admin.AdminNotFoundException;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.SameEmail.SameEmailFoundException;
import com.erp.Exception.User.AccountManagerLimitExceededException;
import com.erp.Exception.User.TechnicianLimitExceededException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.User.UserMapper;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Meta.MetaUser;
import com.erp.Meta.MetaUserRepository;
import com.erp.Model.*;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Role.RoleRepository;
import com.erp.Repository.RoleActionPermission.RoleActionPermissionRepository;
import com.erp.Repository.SubscriptionModule.SubscriptionRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Repository.UserPermission.UserPermissionRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.UserPermission.UserPermissionService;
import com.erp.Utility.inerfaces.S3StorageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServices {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserIdentity userIdentity;
    private final MetaUserRepository metaUserRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MetaAdminRepository metaAdminRepository;
    private final BranchRepository branchRepository;
    private final UserCustomRepository userCustomRepository;
    private final static String DEFAULT_ROLE = "EMPLOYEE";
    private final S3StorageService s3StorageService;
    private final S3Presigner s3Presigner;


    RoleActionPermissionRepository roleActionPermissionRepository;

    UserPermissionRepository userPermissionRepository;

    private final UserPermissionService userPermissionService;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        GenericUser genericUser = userIdentity.getCurrentUser();
        String schemaName = genericUser.getSchemaName();
        TenantContext.setCurrentTenant(schemaName);

        try {
            if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                throw new SameEmailFoundException("Employee already exists with this email");
            }
            if (metaUserRepository.findByUserEmail(userRequest.getEmail()).isPresent()) {
                throw new SameEmailFoundException("Employee already exists with this email");
            }

            SubscriptionEntity subscription = getSubscriptionDetails();

            boolean isTechnician = userRequest.getRoles().stream()
                    .anyMatch(r -> r.getRoleName().equalsIgnoreCase("TECHNICIAN"));

            if(isTechnician)
            {
                long totalTechnicians = userRepository.countByIsActiveTrueAndRoles_RoleName("TECHNICIAN");
                if(totalTechnicians >= Integer.parseInt(subscription.getTotalTechnicians()))
                {
                    throw new TechnicianLimitExceededException("You purchased only "+subscription.getTotalTechnicians()+" Technicians, You have already "+subscription.getTotalTechnicians()+" ACTIVE Technicians, Now You can not create more than this");
                }
            }

            boolean isAccountManager = userRequest.getRoles().stream()
                    .anyMatch(r -> r.getRoleName().equalsIgnoreCase("ACCOUNTMANAGER"));

            if(isAccountManager)
            {
                long totalAccountManager = userRepository.countByIsActiveTrueAndRoles_RoleName("ACCOUNTMANAGER");
                if(totalAccountManager >= Integer.parseInt(subscription.getAccountUser()))
                {
                    throw new AccountManagerLimitExceededException("You purchased only "+subscription.getAccountUser()+" Account Manager, You have already "+subscription.getAccountUser()+" ACTIVE Account Manager, Now You can not create more than this");
                }
            }

            Branch branch = branchRepository.findById(userRequest.getBranchId())
                    .orElseThrow(() -> new BranchNotFoundException("Branch Not Found!!"));

            MetaUser metaUser = new MetaUser();

            metaUser.setUserEmail(userRequest.getEmail());
            metaUser.setSchemaName(schemaName);

            metaUserRepository.save(metaUser);

            User user = userMapper.mapToUser(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRoles(new HashSet<>());
            user.setDesignation(userRequest.getDesignation());
            user.setCreatedByAdminId(genericUser.getId());
            user.setSchemaName(schemaName);

            user.setBranchName(branch.getBranchName());
            user.setReportingTo(userRequest.getReportingTo());
            user.setModuleName(userRequest.getModuleName());

            user.setBranch(branch);
            if(userRequest.isBranchAdmin())
                user.setBranchAdmin(true);
            else if(userRequest.isManager()){
                user.setManager(true);
                user.setReporterId(genericUser.getId());
            }
            else{
                User reporter = userRepository.findByIdAndIsActiveTrue(userRequest.getReporterId());
                if(reporter == null)
                    throw new UserNotFoundException("Reporter Not Found!!");

                user.setReporterId(reporter.getId());
                user.setReportingTo(reporter.getFirstName()+" "+reporter.getLastName());
            }

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

            for (Role role : user.getRoles()) {
                userPermissionService.addUserPermisionBasedOnRole(user.getId(), role.getRoleName());
            }

            return toResponse(user);

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        } finally {
            TenantContext.clear();
        }
    }


    @Override
    @Transactional()
    public List<UserResponse> getListOfUsers() {

        List<User> users = userRepository.findByIsActiveTrue();
        List<UserResponse> list = new ArrayList<>();

        for (User user : users) {

            list.add(toResponse(user));
        }

        return list;
    }

    @Transactional
    @Override
    public UserResponse updateUserById(UserUpdateRequest userUpdateRequest) throws Exception {

        Admin currentAdmin = (Admin) userIdentity.getCurrentUser();

        // User user = (User) userIdentity.getCurrentUser();

        User user = userRepository.findByIdAndIsActiveTrue(userUpdateRequest.getId());

        if (user == null)
            throw new UserNotFoundException("User Not Found");

        if (user.getId() == userUpdateRequest.getId()) {

            // Update only allowed fields
            if (userUpdateRequest.getFirstName() != null)
                user.setFirstName(userUpdateRequest.getFirstName());

            if (userUpdateRequest.getLastName() != null)
                user.setLastName(userUpdateRequest.getLastName());

            if (userUpdateRequest.getPhoneNo() != 0)
                user.setPhoneNo(userUpdateRequest.getPhoneNo());

        } else {
            throw new UserNotFoundException(
                    "With this user id: " + userUpdateRequest.getId() + " user is currently not login !");
        }

        Branch branch = branchRepository.findById(userUpdateRequest.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found"));

        // Update branch
        user.setBranch(branch);
        user.setBranchName(branch.getBranchName());

        // ---------------- COMMENTED CODE (as requested) ----------------

        // Set<Role> updatedRoles = new HashSet<>();
        // for (RoleRequest roleReq : userUpdateRequest.getRoles()) {
        //     String roleNameUpper = roleReq.getRoleName().toUpperCase();
        //     Role role = roleRepository.findByRoleName(roleNameUpper)
        //             .orElseGet(() -> {
        //                 Role newRole = new Role();
        //                 newRole.setRoleName(roleNameUpper);
        //                 return roleRepository.save(newRole);
        //             });
        //     updatedRoles.add(role);
        // }

        // Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE)
        //         .orElseGet(() -> {
        //             Role newRole = new Role();
        //             newRole.setRoleName(DEFAULT_ROLE);
        //             return roleRepository.save(newRole);
        //         });
        // updatedRoles.add(defaultRole);

        // user.setRoles(updatedRoles);

        userRepository.save(user);

        // List<String> roleNames =
        //         userUpdateRequest.getRoles().stream()
        //                 .map(RoleRequest::getRoleName)
        //                 .collect(Collectors.toList());

        // userPermissionService.updateUserRolePermissionByRoleName(roleNames, user.getId());

        // updateUserModuleActionPermissions(user, updatedRoles, userUpdateRequest.getPermissions(), currentAdmin);

        return toResponse(user);
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
                .orElseThrow(() -> new UserNotFoundException("User not found with this id: " + commanParamId.getId()));

        user.setActive(false);
        userRepository.save(user);
        return toResponse(user);

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

    // Utility Function
    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNo(user.getPhoneNo());
        response.setActive(user.isActive());
        response.setSchemaName(user.getSchemaName());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastModifiedAt(user.getLastModifiedAt());
        response.setBranchName(user.getBranchName());
        response.setModuleName(user.getModuleName());
        response.setReportingTo(user.getReportingTo());
        response.setFullName(user.getFirstName()+" "+user.getLastName());
        response.setBranchId(user.getBranch().getBranchId());
        response.setReporterId(user.getReporterId());

        // Roles → List<String>
        List<String> roleNames = user.getRoles()
                .stream()
                .map(role -> role.getRoleName())
                .toList();

        response.setRoleNames(roleNames);

        String s3Key = generatePresignedUrl(user.getDocumentUrl());
        response.setProfileUrl(s3Key);

        return response;
    }

    @Override
    public UserResponse getByEmail() {
        String email = userIdentity.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        return toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserProfileRequest userProfileRequest, MultipartFile[] files) {
        String email = userIdentity.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        user.setFirstName(userProfileRequest.getFirstName());
        user.setLastName(userProfileRequest.getLastName());
        user.setPhoneNo(userProfileRequest.getPhoneNo());

        if (files != null && files.length > 0) {
            List<FileUploadResponse> fileUploadResponses =
                    s3StorageService.uploadFile(files, "user/profile");

            if (!fileUploadResponses.isEmpty()) {
                user.setDocumentUrl(fileUploadResponses.get(0).getS3Key());
            }
        }

        return toResponse(user);
    }

    private SubscriptionEntity getSubscriptionDetails()
    {
        String schemaName = TenantContext.getCurrentTenant();

        String email = metaAdminRepository.findAdminEmailBySchemaName(schemaName)
                .orElseThrow( () -> new AdminNotFoundException("Schema Not Found With : "+schemaName));

        SubscriptionEntity subscription = subscriptionRepository.findByUserId(email)
                .orElseThrow( () -> new ResourceNotFoundException("Subscription Not Found for Email : "+email));

        return subscription;
    }

    @Override
    public ResultDto<DropDown> getUserDropDownList(String id) {
        ResultDto<DropDown> result = new ResultDto<>();

        if(id.equals("") || id.isEmpty()){
            result.setCount(0);
            result.setResults(new ArrayList<>());
            return result;
        }

        long idVal = Long.parseLong(id);

        if(!branchRepository.existsById(idVal))
            throw new BranchNotFoundException("Branch Not Found");

        List<User> managers = userRepository.findByIsManagerTrueAndIsActiveTrueAndBranch_BranchId(idVal);
        List<DropDown> dropdown = new ArrayList<>();

        for(User manager : managers){
            dropdown.add(new DropDown(manager.getId(), manager.getFirstName()+" "+manager.getLastName()));
        }
        result.setResults(dropdown);
        result.setCount(dropdown.size());
        return result;
    }

    @Override
    public ResultDto<UserResponse> getUsersBranchWise() {

        GenericUser genericUser = userIdentity.getCurrentUser();
        User user = userRepository.findByEmail(genericUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found!!"));

        List<User> users = userRepository.findByBranch_BranchId(user.getBranch().getBranchId());
        List<UserResponse> userResponseList = new ArrayList<>();
        for(User u : users)
            userResponseList.add(toResponse(u));

        ResultDto<UserResponse> resultDto = new ResultDto<>();
        resultDto.setCount(userResponseList.size());
        resultDto.setResults(userResponseList);
        return resultDto;
    }

    @Override
    public ResultDto<UserResponse> getUsersFilterWise(FilterRequest filterRequest) {
        return userCustomRepository.filterUsers(filterRequest);
    }

    private String generatePresignedUrl(String s3Key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(p -> p
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));

        return presignedRequest.url().toString();
    }
}