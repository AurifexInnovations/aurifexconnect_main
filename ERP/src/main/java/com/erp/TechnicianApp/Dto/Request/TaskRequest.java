package com.erp.TechnicianApp.Dto.Request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequest {

    private String clientName;
    private String clientLocation;
    private String serviceType;
    private LocalDateTime scheduleTime;
    /*private String status;*/
    private String feedback;

}
