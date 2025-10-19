package com.erp.Dto.Request;


import lombok.Data;
import java.time.LocalDate;

@Data
public class TechnicianTaskRequest {

    private LocalDate assignedDate;
    private Long technicianId;


    private Integer page = 0;
    private Integer size = 10;

    public int getOffset() {
        return page * size;
    }
}
