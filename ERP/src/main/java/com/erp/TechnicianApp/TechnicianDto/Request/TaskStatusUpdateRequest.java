package com.erp.TechnicianApp.TechnicianDto.Request;

import com.erp.TechnicianApp.TechnicianModel.TechnicianTask.TechnicianTaskStatus;
import lombok.Data;

@Data
public class TaskStatusUpdateRequest {
    private TechnicianTaskStatus status; // must be nested enum type
}

