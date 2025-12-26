package com.erp.Dto.Request;

import com.erp.Model.FileInfoDto;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileRequest {
    private String firstName;
    private String lastName;
    private long phoneNo;
    private List<FileInfoDto> files;
}
