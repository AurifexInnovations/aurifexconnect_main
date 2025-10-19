package com.erp.Mapper.TaskMapper;

import com.erp.Dto.Request.TaskRequest;
import com.erp.Model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TaskMapper {

    Task mapToTask(TaskRequest taskRequest);

    void mapToTaxEntity(TaskRequest taskRequest, @MappingTarget Task task);


}
