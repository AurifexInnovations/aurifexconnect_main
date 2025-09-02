package com.erp.TechnicianApp.TechnicianMapper.TechnicianLocation;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianLocationResponse;
import com.erp.TechnicianApp.TechnicianModel.TechnicianLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnicianLocationMapper {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "attendance.id", source = "attendanceId")
    @Mapping(target = "task.taskId", source = "taskId")
    TechnicianLocation toEntity(TechnicianLocationRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "attendanceId", source = "attendance.id")
    @Mapping(target = "taskId", source = "task.taskId")
    TechnicianLocationResponse toResponse(TechnicianLocation location);

    List<TechnicianLocationResponse> toResponseList(List<TechnicianLocation> locations);
}
