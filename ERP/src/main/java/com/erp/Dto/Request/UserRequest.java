package com.erp.Dto.Request;

import com.erp.Dto.Constraints.ContactNumber;
import com.erp.Dto.Constraints.Name;
import com.erp.Dto.Constraints.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @NotEmpty(message = "Roles cannot be empty")
    @Size(min = 1, message = "User must have at least one role")
    private Set<RoleRequest> roles = new HashSet<>();


    @NotEmpty(message = "At least one module must be selected")
    private Set<String> modules = new HashSet<>();

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