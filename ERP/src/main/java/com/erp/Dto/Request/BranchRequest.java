package com.erp.Dto.Request;

import com.erp.Enum.BranchStatus;
import com.erp.Enum.BranchType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BranchRequest {
    private long id;
    private String branchName;
    private String contactInfo;
    private String phoneNumber;
    private BranchStatus branchStatus;
    private BranchType branchType;
    private String pincode;
    private String city;
    private String state;
    private String location;
    private String editedBy;
}
