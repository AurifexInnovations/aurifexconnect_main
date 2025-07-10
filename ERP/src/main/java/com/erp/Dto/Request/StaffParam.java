package com.erp.Dto.Request;

import com.erp.Enum.Designation;
import com.erp.Enum.StaffStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffParam {

    private long id;
    private String name;
    private Designation designation;
    private StaffStatus staffStatus;
    private long branchId;
}
