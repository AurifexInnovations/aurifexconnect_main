package com.erp.Dto.Response;


import com.erp.Enum.LeaveType;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
public class LeaveResponse {
    private long id;
    private UserResponse user;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;
    private String reason;
    private String status;
}
