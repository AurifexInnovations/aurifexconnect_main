package com.erp.Service.LeaveService.LeaveServiceNotification;

import com.erp.Model.Leave;

public interface LeaveServiceNotification {

    void notifyLeaveCreated(Leave leave);

    void notifyLeaveUpdated(Leave leave);

    void notifyLeaveDeleted(Leave leave);

    void notifyLeaveStatusChanged(Leave leave, String oldStatus, String newStatus);
}
