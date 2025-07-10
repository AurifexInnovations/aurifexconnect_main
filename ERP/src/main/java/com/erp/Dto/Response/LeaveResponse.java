package com.erp.Dto.Response;
import com.erp.Enum.LeaveType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
public class LeaveResponse {
    private long id;
    private UserResponse user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private LeaveType leaveType;
    private String reason;
    private String status;
}
