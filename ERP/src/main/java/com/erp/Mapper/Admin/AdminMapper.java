package com.erp.Mapper.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Dto.Response.AdminResponse;
import com.erp.Dto.Response.AdminUpdateRequest;
import com.erp.Model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin mapToAdmin(AdminRequest adminRequest);

    AdminResponse mapToAdminResponse (Admin admin);

    void mapToAdminEntity(AdminUpdateRequest adminUpdateRequest , @MappingTarget Admin  admin);

    List<AdminResponse> mapToListOfAdminResponse(List<Admin> admins);
}