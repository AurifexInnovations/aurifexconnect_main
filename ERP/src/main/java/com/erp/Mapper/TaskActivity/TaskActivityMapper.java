package com.erp.Mapper.TaskActivity;

import com.erp.Dto.Request.TaskActivityRequest;
import com.erp.Dto.Response.TaskActivityResponse;
import com.erp.Model.TaskActivity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskActivityMapper {

    TaskActivity mapToEntity(TaskActivityRequest request);

    void mapToEntityUpdate(TaskActivityRequest request, @MappingTarget TaskActivity taskActivity);

    TaskActivityResponse mapToResponse(TaskActivity taskActivity);
}