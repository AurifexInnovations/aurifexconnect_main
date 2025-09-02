package com.erp.TechnicianApp.TechnicianDto.Request;

import com.erp.TechnicianApp.TechnicianEnum.TechnicianTaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusUpdateRequest {
    private TechnicianTaskStatus status;
}
