package com.erp.Mapper.Staff;

import com.erp.Dto.Request.StaffRequest;
import com.erp.Dto.Response.StaffResponse;
import com.erp.Model.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface StaffMapper {

    // Map StaffRequest to Staff Entity
    Staff mapToStaff(StaffRequest staffRequest);

    // Update existing Staff entity with StaffRequest data
    void mapToStaffEntity(StaffRequest staffRequest, @MappingTarget Staff staff);

    // Map Staff Entity to StaffResponse
    @Mapping(source = "branch.branchName", target = "branchName")
    StaffResponse mapToStaffResponse(Staff staff);

    // Map List<Staff> to List<StaffResponse>
    List<StaffResponse> mapToStaffResponse(List<Staff> staffList);
}
