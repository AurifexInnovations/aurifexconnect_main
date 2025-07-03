package com.erp.Dto.Request;

import com.erp.Enum.LeaveStatus;
import com.erp.Enum.LeaveType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequest {
    private long id;
    private long userId;
    private String firstName;
    private String lastName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;
    private String reason;
    private LeaveStatus status;

}