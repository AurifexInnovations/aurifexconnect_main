package com.erp.Dto.Request;


import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TechnicianRequest {

    @Size(max = 20, message = "Status must be at most 20 characters")
    private String status;

    @Size(max = 50, message = "Category must be at most 50 characters")
    private String category;

    private LocalDate startDate;
    private LocalDate endDate;

    // Pagination parameters
    private Integer page = 0;   // default to page 0
    private Integer size = 10;  // default page size

    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }

    public int getOffset() {
        return page * size; // calculate offset for SQL query
    }
}