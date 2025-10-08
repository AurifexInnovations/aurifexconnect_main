package com.erp.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public interface TechnicianResponse {

    Long getId();

    String getName();

    String getEmail();

    String getPhone();

    String getCategory();

    String getDesignation();

    Boolean getStatus();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate getCreatedAt();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate getUpdatedAt();
}
