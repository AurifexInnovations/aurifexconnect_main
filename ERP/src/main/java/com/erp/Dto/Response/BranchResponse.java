package com.erp.Dto.Response;

import com.erp.Enum.BranchStatus;
import com.erp.Enum.BranchType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BranchResponse
{
    private Long branchId;
    private String branchName;
    private String contactInfo;
    private String phoneNumber;
    private BranchStatus branchStatus;
    private BranchType branchType;
    private String editedBy;
    private String pincode;
    private String city;
    private String state;
    private String location;
    private LocalDateTime createdAt;
}
