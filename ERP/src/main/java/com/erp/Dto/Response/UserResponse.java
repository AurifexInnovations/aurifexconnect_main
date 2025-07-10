package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserResponse {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private long phoneNo;

    private boolean isActive;

    private LocalDate createdAt;

    private LocalDate lastModifiedAt;

    private List<String> roleNames;


}
