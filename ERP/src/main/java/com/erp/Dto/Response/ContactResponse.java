package com.erp.Dto.Response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long accountId;
    private String companyName;
    private String position;
    private LocalDateTime createdAt;
}