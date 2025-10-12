package com.erp.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public interface GetAllTaskResponse {

    Long getTaskId();

    String getTaskName();

    String getStatus();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDate getAssignedDate();

    String getServiceLocation();
}
