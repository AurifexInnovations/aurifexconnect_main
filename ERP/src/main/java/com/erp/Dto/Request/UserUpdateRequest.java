package com.erp.Dto.Request;

import com.erp.Dto.Constraints.ContactNumber;
import com.erp.Dto.Constraints.Name;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    private long id;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private long phoneNo;
}
