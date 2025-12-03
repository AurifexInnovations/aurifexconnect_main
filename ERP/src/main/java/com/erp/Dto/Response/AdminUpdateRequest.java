package com.erp.Dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateRequest {
    private long id;
    private String name;
    private long contactNo;
}
