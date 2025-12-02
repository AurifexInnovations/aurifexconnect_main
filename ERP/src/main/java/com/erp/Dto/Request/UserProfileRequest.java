package com.erp.Dto.Request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {
    private String firstName;
    private String lastName;
    private long phoneNo;
}
