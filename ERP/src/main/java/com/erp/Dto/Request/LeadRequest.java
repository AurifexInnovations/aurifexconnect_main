package com.erp.Dto.Request;

import com.erp.Enum.LeadStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadRequest {
    @NotBlank
    private String firstName;

    private String lastName;

    @Email
    @NotBlank
    private String email;

    private String phone;
    private String source;
    private LeadStatus status;
    private Long assignedToId;
    private String assignedToName;
}