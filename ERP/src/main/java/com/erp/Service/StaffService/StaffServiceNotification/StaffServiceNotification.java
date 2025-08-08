package com.erp.Service.StaffService.StaffServiceNotification;

import com.erp.Model.Staff;

public interface StaffServiceNotification {
    void notifyStaffCreated(Staff staff);
    void notifyStaffUpdated(Staff staff);
    void notifyStaffDeleted(Staff staff);
}
