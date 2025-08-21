package com.erp.Dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactRequest {
    private Long id;

    @NotBlank
    private String firstName;
    private String lastName;

    @Email
    private String email;
    private String phone;
    private Long accountId;
}
