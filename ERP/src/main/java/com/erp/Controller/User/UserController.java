package com.erp.Controller.User;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.UserResponse;
import com.erp.Service.User.UserServices;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${app.base-url}")
public class UserController {

    private final UserServices userServices;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BRANCHADMIN','ROLE_HR','ROLE_ACCOUNTANT','ROLE_SALESADMIN','ROLE_TECHNICIANMANAGER')")
    @PostMapping("/users")
    public ResponseEntity<ResponseStructure<UserResponse>> createUser(@RequestBody UserRequest userRequest) {

        UserResponse userResponse = userServices.createUser(userRequest);
        return ResponseBuilder.success(HttpStatus.CREATED, "User created successfully !!", userResponse);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ListResponseStructure<UserResponse>> getListOfUsers(){

        List<UserResponse> userResponses = userServices.getListOfUsers();
        return ResponseBuilder.success(HttpStatus.OK,"List of users !!", userResponses);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/users/search")
    public ResponseEntity<ListResponseStructure<UserResponse>> findByIdOrName(@RequestBody CommanParam commanParamIdOrName){

        List<UserResponse> userResponses = userServices.findByIdOrName(commanParamIdOrName);
        return ResponseBuilder.success(HttpStatus.OK,"User found !!", userResponses);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/users")
    public ResponseEntity<ResponseStructure<UserResponse>> updateUserById
            (@RequestBody UserUpdateRequest userUpdateRequest) throws Exception {

        UserResponse userResponse = userServices.updateUserById(userUpdateRequest);
        return ResponseBuilder.success(HttpStatus.OK,"User Updated Successfully !!", userResponse);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/users")
    public ResponseEntity<ResponseStructure<UserResponse>> deleteUserById(@RequestBody CommanParam commanParamId){

        UserResponse userResponse = userServices.deleteUserById(commanParamId);
        return ResponseBuilder.success(HttpStatus.OK,"User delete Successfully !!", userResponse);

    }

    @GetMapping("/users/byEmail")
    public ResponseEntity<ResponseStructure<UserResponse>> findByEmail()
    {
        UserResponse userResponse = userServices.getByEmail();
        return ResponseBuilder.success(HttpStatus.OK, "User Fetched Successfully!!", userResponse);
    }

    @PutMapping(value = "/user/profile/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseStructure<UserResponse>> updateUser(
            @RequestPart String firstName,
            @RequestPart String lastName,
            @RequestPart String phoneNo,
            @RequestPart(value = "profile", required = false) MultipartFile[] files) {
        if (files == null) {
            files = new MultipartFile[0]; // ensure non-null array
        }
        UserResponse response = userServices.updateUser(firstName, lastName, Long.parseLong(phoneNo), files);
        return ResponseBuilder.success(HttpStatus.OK, "User Updated Successfully!!", response);
    }

    @GetMapping("/user/dropdown")
    public ResponseEntity<ResponseStructure<ResultDto<DropDown>>> getUserDropDown(@RequestParam String id){
        ResultDto<DropDown> dropdown = userServices.getUserDropDownList(id);
        return ResponseBuilder.success(HttpStatus.OK, "Manager Drop Down List", dropdown);
    }

    @GetMapping("/user/branchWise")
    public ResponseEntity<ResponseStructure<ResultDto<UserResponse>>> getUsersBranchWise(){
        ResultDto<UserResponse> resultDto = userServices.getUsersBranchWise();
        return ResponseBuilder.success(HttpStatus.OK, "Fetched Users Successfully", resultDto);
    }

    @PostMapping("/user/filter")
    public ResponseEntity<ResponseStructure<ResultDto<UserResponse>>> getUserFilterWise(@RequestBody FilterRequest filterRequest){
        ResultDto<UserResponse> resultDto = userServices.getUsersFilterWise(filterRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Filter Wise Fetched Users Successfully", resultDto);
    }
}