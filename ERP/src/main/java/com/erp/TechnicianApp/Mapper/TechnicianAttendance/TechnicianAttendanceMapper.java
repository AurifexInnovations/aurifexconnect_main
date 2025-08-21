package com.erp.TechnicianApp.Mapper.TechnicianAttendance;


import com.erp.TechnicianApp.Dto.Request.TechnicianAttendanceRequest;
import com.erp.TechnicianApp.Dto.Response.TechnicianAttendanceResponse;
import com.erp.TechnicianApp.Model.TechnicianAttendance.TechnicianAttendance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnicianAttendanceMapper {

    TechnicianAttendance mapToEntity(TechnicianAttendanceRequest request);

    TechnicianAttendanceResponse mapToResponse(TechnicianAttendance technicianAttendance);

    List<TechnicianAttendanceResponse> mapToResponseList(List<TechnicianAttendance> technicianAttendances);

}
