package com.erp.TechnicianApp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TaskStatusUpdateRequest {
    private String status;
    private String comment;
}
