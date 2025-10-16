package com.erp.Service.User;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.UserRequest;
import com.erp.Dto.Request.UserUpdateRequest;
import com.erp.Dto.Response.UserResponse;

import java.util.List;

public interface UserServices {

    UserResponse createUser(UserRequest userRequest);

    List<UserResponse> getListOfUsers();

    UserResponse updateUserById(UserUpdateRequest userUpdateRequest) throws Exception;

    UserResponse deleteUserById(CommanParam commanParamId);

    List<UserResponse> findByIdOrName(CommanParam commanParamIdOrName);
}
