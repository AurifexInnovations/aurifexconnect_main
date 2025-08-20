package com.erp.Dto.Response;

import com.erp.Enum.LeadStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeadResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String source;
    private LeadStatus status;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
