package com.erp.Service.User.UserNotification;

import com.erp.Model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserNotification {

    public void notifyUserCreated(User user) {
        log.info("✅ User created: ID={}, Email={}", user.getId(), user.getEmail());
    }

    public void notifyUserUpdated(User user) {
        log.info("✏️ User updated: ID={}, Email={}", user.getId(), user.getEmail());
    }

    public void notifyUserDeleted(User user) {
        log.info("❌ User marked as inactive (soft deleted): ID={}, Email={}", user.getId(), user.getEmail());
    }

    public void notifyUserSearchPerformed(String criteria) {
        log.info("🔍 User search performed with criteria: {}", criteria);
    }
}