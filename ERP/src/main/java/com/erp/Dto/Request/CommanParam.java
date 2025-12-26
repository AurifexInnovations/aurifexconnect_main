package com.erp.Dto.Request;

import com.erp.Enum.BranchStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommanParam {

    private Long id;
    private String name;
    private String location;
    private BranchStatus branchStatus;
}
