package com.erp.Dto.Request;

import com.erp.Enum.BranchStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommanParam {

    private long id;
    private String name;
    private String location;
    private BranchStatus branchStatus;
}
