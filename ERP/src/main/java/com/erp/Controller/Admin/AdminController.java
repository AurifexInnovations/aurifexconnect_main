package com.erp.Controller.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Response.AdminResponse;
import com.erp.Dto.Response.AdminUpdateRequest;
import com.erp.Service.Admin.AdminService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

//    @PreAuthorize("hasAuthority('ROLE_ROOT')")
    @PostMapping("/admins")
    public ResponseEntity<ResponseStructure<AdminResponse>> createAdmin(@Valid @RequestBody AdminRequest adminRequest){
        AdminResponse adminResponse = adminService.createAdmin(adminRequest);
        return ResponseBuilder.success(HttpStatus.CREATED,"Admin created successfully !!",adminResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_ROOT')")
    @GetMapping("/all/admins")
    public ResponseEntity<ListResponseStructure<AdminResponse>> getListOfAdmins(){
        List<AdminResponse> adminResponseList = adminService.getListOfAdmins();
        return ResponseBuilder.success(HttpStatus.OK,"List of admins !!",adminResponseList);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/admin")
    public ResponseEntity<ResponseStructure<AdminResponse>> updateAdminById(@RequestBody AdminUpdateRequest adminUpdateRequest) {
        AdminResponse adminResponse = adminService.updateAdminById(adminUpdateRequest, adminUpdateRequest.getFiles());
        return ResponseBuilder.success(HttpStatus.OK,"Admin details updated successfully !!",adminResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_ROOT')")
    @DeleteMapping("/admins/delete")
    public ResponseEntity<ResponseStructure<AdminResponse>> deleteAdminById(@RequestBody CommanParam commanParam){
        AdminResponse adminResponse = adminService.deleteAdminById(commanParam);
        return ResponseBuilder.success(HttpStatus.OK,"Admin delete Successfully !!",adminResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getDetails/admin")
    public ResponseEntity<ResponseStructure<AdminResponse>> getById()
    {
        AdminResponse adminResponse = adminService.findAdminById();
        return ResponseBuilder.success(HttpStatus.OK, "Admin Fetched Successfully!!", adminResponse);
    }
 }