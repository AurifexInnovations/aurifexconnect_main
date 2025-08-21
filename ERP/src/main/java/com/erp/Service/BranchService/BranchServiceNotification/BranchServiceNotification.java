package com.erp.Service.BranchService.BranchServiceNotification;

import com.erp.Model.Branch;

public interface BranchServiceNotification {
    void notifyBranchCreated(Branch branch);

    void notifyBranchUpdated(Branch branch);

    void notifyBranchDeleted(Branch branch);
}
