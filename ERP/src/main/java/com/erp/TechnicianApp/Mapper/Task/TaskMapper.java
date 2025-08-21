package com.erp.TechnicianApp.Mapper.Task;


import com.erp.TechnicianApp.Dto.Request.TaskRequest;
import com.erp.TechnicianApp.Dto.Response.TaskResponse;
import com.erp.TechnicianApp.Model.Task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface TaskMapper {


    Task mapToEntity(TaskRequest taskRequest);

    @Mapping(source = "technician.technicianId", target = "technicianId")
    @Mapping(source = "technician.technicianName", target = "technicianName")
    TaskResponse mapToResponse(Task task);

    List<TaskResponse> mapToResponseList(List<Task> tasks);

    void updateTaskFromRequest(TaskRequest taskRequest, @MappingTarget Task task);
}
