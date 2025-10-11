package com.erp.Dto.Request;

import com.erp.Dto.Constraints.ContactNumber;
import com.erp.Dto.Constraints.Name;
import com.erp.Dto.Constraints.Password;
import com.erp.Dto.Request.RoleRequest;

import com.erp.Enum.Designation;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class UserRequest {

    @Name
    private String firstName;

    @Name
    private String lastName;

    @Email
    private String email;

    @ContactNumber
    private long phoneNo;

    @Password
    private String password;

    private Designation designation;

    @NotEmpty(message = "Roles cannot be empty")
    @Size(min = 1, message = "User must have at least one role")
    private Set<RoleRequest> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequest that = (UserRequest) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}