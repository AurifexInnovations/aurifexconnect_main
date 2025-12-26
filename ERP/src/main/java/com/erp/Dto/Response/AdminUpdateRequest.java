package com.erp.Dto.Response;

import com.erp.Model.FileInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminUpdateRequest {
    private String name;
    private long contactNo;
    private List<FileInfoDto> files;
}
