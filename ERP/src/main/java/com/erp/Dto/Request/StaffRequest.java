package com.erp.Dto.Request;

import com.erp.Enum.Designation;
import com.erp.Enum.StaffStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffRequest {

    private long id;  // for update, can be skipped for create

    private String staffName;

    private String email;

    private String contactNo;

    private Designation designation;

    private StaffStatus staffStatus;

    private long branchId; // the branch to which this staff belongs
}
