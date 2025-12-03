package com.erp.Service.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Response.AdminResponse;
import com.erp.Dto.Response.AdminUpdateRequest;

import java.util.List;

public interface AdminService {

    AdminResponse createAdmin(AdminRequest adminRequest);

    List<AdminResponse> getListOfAdmins();

    AdminResponse updateAdminById(AdminUpdateRequest adminUpdateRequest);

    AdminResponse deleteAdminById(CommanParam commanParam);

    AdminResponse findAdminById();
}
