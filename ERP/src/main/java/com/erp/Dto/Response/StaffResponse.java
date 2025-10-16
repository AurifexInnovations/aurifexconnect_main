package com.erp.Dto.Response;

import com.erp.Enum.Designation;
import com.erp.Enum.StaffStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffResponse {

    private long id;

    private String staffName;

    private String email;

    private String contactNo;

    private Designation designation;

    private StaffStatus staffStatus;

    private String branchName;  // so API consumer can easily view which branch this staff belongs to
}
