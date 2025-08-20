package com.erp.Service.SalaryService.SalaryServiceNotification;

import com.erp.Model.Salary;

public interface SalaryServiceNotification {

    void notifySalaryGenerated(Salary salary);

    void notifySalaryUpdated(Salary salary);

    void notifySalaryDeleted(Salary salary);

    void notifySalaryMarkedAsPaid(Salary salary);
}
