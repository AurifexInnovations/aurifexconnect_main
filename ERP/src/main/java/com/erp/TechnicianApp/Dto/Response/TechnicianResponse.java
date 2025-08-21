package com.erp.TechnicianApp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianResponse {

    private long technicianId;
    private String technicianName;
    private String email;
    private String password;
    private int age;
    private String role;
    private String mobileNumber;
    private String panNumber;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdateAt;
}
