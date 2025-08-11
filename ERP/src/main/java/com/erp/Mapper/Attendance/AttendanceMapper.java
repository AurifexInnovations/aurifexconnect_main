package com.erp.Mapper.Attendance;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Model.Attendance;
import com.erp.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    AttendanceResponse mapToResponse(Attendance attendance);

    List<AttendanceResponse> mapToAttendanceResponse(List<Attendance> attendances);

}
