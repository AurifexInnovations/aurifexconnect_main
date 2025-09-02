package com.erp.TechnicianApp.TechnicianMapper.Task;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianTaskRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianTaskResponse;
import com.erp.TechnicianApp.TechnicianModel.TechnicianTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TechnicianTaskMapper {

    TechnicianTask toEntity(TechnicianTaskRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    @Mapping(source = "user.email", target = "userEmail")
    TechnicianTaskResponse toResponse(TechnicianTask task);

    List<TechnicianTaskResponse> toResponseList(List<TechnicianTask> tasks);
}
