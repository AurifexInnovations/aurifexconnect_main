package com.erp.Service.StaffService.StaffServiceNotification;

import com.erp.Model.Staff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StaffServiceNotificationImpl implements StaffServiceNotification {

    @Override
    public void notifyStaffCreated(Staff staff) {
        log.info("📢 Staff created: {}", staff.getStaffName());
    }

    @Override
    public void notifyStaffUpdated(Staff staff) {
        log.info("✏️ Staff updated: {}", staff.getStaffName());
    }

    @Override
    public void notifyStaffDeleted(Staff staff) {
        log.info("❌ Staff deleted: {}", staff.getStaffName());
    }
}
