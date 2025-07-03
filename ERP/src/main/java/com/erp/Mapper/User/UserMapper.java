package com.erp.Mapper.User;

import com.erp.Dto.Request.UserRequest;
import com.erp.Dto.Request.UserUpdateRequest;
import com.erp.Dto.Response.UserResponse;
import com.erp.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(UserRequest userRequest);

    @Mapping(target = "roleNames",
            expression = "java(user.getRoles() != null ? user.getRoles().stream().map(com.erp.Model.Role::getRoleName).collect(java.util.stream.Collectors.toList()) : java.util.Collections.emptyList())")
    UserResponse mapToUserResponse(User user);

    List<UserResponse> mapToListOfUserResponse(List<User> users);

    void mapTOUserEntity(UserUpdateRequest userUpdateRequest, @MappingTarget User use);

}