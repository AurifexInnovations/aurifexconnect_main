package com.erp.Repository.TaskActivity;

import com.erp.Enum.TaskStatus;
import com.erp.Enum.TaskType;
import com.erp.Model.TaskActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskActivityRepository extends JpaRepository<TaskActivity, Long> {
    List<TaskActivity> findByStatus(TaskStatus status);
    List<TaskActivity> findByType(TaskType type);
    List<TaskActivity> findByDueDate(LocalDate date);
    List<TaskActivity> findByRelatedLeadId(Long leadId);
    List<TaskActivity> findByRelatedContactId(Long contactId);
    List<TaskActivity> findByRelatedDealId(Long dealId);
}
