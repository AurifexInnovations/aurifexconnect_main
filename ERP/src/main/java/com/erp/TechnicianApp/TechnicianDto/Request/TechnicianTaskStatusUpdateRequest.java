package com.erp.TechnicianApp.TechnicianDto.Request;


import com.erp.TechnicianApp.TechnicianModel.TechnicianTask.TechnicianTaskStatus;
import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class TechnicianTaskStatusUpdateRequest {
    private TechnicianTaskStatus status;
    private String comment;
}
