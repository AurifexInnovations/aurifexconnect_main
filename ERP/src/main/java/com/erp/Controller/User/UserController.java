package com.erp.Controller.User;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.UserRequest;
import com.erp.Dto.Request.UserUpdateRequest;
import com.erp.Dto.Response.UserResponse;
import com.erp.Service.User.UserServices;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${app.base-url}")
public class UserController {

    private final UserServices userServices;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('EMPLOYEE')")
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

}