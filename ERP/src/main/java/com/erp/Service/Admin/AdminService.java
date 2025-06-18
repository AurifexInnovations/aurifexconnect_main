package com.erp.Service.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Response.AdminResponse;

import java.util.List;

public interface AdminService {

    AdminResponse createAdmin(AdminRequest adminRequest);

    List<AdminResponse> getListOfAdmins();

    AdminResponse updateAdminById(AdminRequest adminRequest);

    AdminResponse deleteAdminById(CommanParam commanParam);

    List<AdminResponse> findAdminByIdOrName(CommanParam commanParam);
}
