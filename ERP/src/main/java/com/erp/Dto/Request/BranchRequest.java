package com.erp.Dto.Request;

import com.erp.Enum.BranchStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchRequest {

    private long id;

    private String branchName;

    private String location;

    private String contactInfo;

    private BranchStatus branchStatus;
}
