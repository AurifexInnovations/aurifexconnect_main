package com.erp.TechnicianApp.Dto.Request;

import lombok.*;

@Getter
@Setter
public class TechnicianRequest {

    private String technicianName;
    private String email;
    private String password;
    private int age;
    private String role;
    private String mobileNumber;
    private String panNumber;
}
