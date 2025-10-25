package com.erp.Dto.Request;

import com.erp.Dto.Constraints.ContactNumber;
import com.erp.Dto.Constraints.Name;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserUpdateRequest {

    private long id;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private long phoneNo;

    private Set<RoleRequest> roles = new HashSet<>();


    private Set<PermissionRequest> permissions = new HashSet<>();
}
