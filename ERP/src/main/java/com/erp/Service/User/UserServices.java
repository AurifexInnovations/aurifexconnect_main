package com.erp.Service.User;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserServices {

    UserResponse createUser(UserRequest userRequest);

    List<UserResponse> getListOfUsers();

    UserResponse updateUserById(UserUpdateRequest userUpdateRequest) throws Exception;

    UserResponse deleteUserById(CommanParam commanParamId);

    List<UserResponse> findByIdOrName(CommanParam commanParamIdOrName);

    UserResponse getByEmail();

    UserResponse updateUser(UserProfileRequest userProfileRequest, MultipartFile[] files);

    ResultDto<DropDown> getUserDropDownList(String id);

    ResultDto<UserResponse> getUsersBranchWise();

    ResultDto<UserResponse> getUsersFilterWise(FilterRequest filterRequest);

    UserResponse updateUser(String firstName, String lastName, long phoneNo, MultipartFile[] files);
}
