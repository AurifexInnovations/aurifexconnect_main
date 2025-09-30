package com.erp.Mapper.User;

import com.erp.Dto.Request.UserRequest;
import com.erp.Dto.Request.UserUpdateRequest;
import com.erp.Dto.Response.UserResponse;
import com.erp.Enum.AssignModule;
import com.erp.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "modules", source = "modules", qualifiedByName = "mapModuleStringsToEnum")
    User mapToUser(UserRequest userRequest);

    @Mapping(target = "roleNames",
            expression = "java(user.getRoles() != null ? user.getRoles().stream().map(com.erp.Model.Role::getRoleName).collect(java.util.stream.Collectors.toList()) : java.util.Collections.emptyList())")
    UserResponse mapToUserResponse(User user);

    List<UserResponse> mapToListOfUserResponse(List<User> users);

    void mapTOUserEntity(UserUpdateRequest userUpdateRequest, @MappingTarget User user);

    @Named("mapModuleStringsToEnum")
    default Set<AssignModule> mapModuleStringsToEnum(Set<String> modules) {
        if (modules == null) return null;
        return modules.stream()
                .map(String::toUpperCase)
                .map(AssignModule::valueOf)
                .collect(Collectors.toSet());
    }
}
