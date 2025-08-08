package com.erp.Service.SalaryService.SalaryServiceNotification;

import com.erp.Model.Salary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SalaryServiceNotificationImpl implements SalaryServiceNotification {

    @Override
    public void notifySalaryGenerated(Salary salary) {
        log.info("💰 Salary generated: User ID = {}, Month = {}, Net Salary = {}",
                salary.getUser().getId(), salary.getMonth(), salary.getNetSalary());
    }

    @Override
    public void notifySalaryUpdated(Salary salary) {
        log.info("✏️ Salary updated: User ID = {}, Month = {}, Net Salary = {}",
                salary.getUser().getId(), salary.getMonth(), salary.getNetSalary());
    }

    @Override
    public void notifySalaryDeleted(Salary salary) {
        log.info("🗑️ Salary deleted: User ID = {}, Month = {}",
                salary.getUser().getId(), salary.getMonth());
    }

    @Override
    public void notifySalaryMarkedAsPaid(Salary salary) {
        log.info("✅ Salary marked as PAID: User ID = {}, Month = {}, Paid Date = {}, Amount = {}",
                salary.getUser().getId(), salary.getMonth(), salary.getPaymentDate(), salary.getNetSalary());
    }
}
