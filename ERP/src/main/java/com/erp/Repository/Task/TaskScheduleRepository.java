package com.erp.Repository.Task;

import com.erp.Model.TaskSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskScheduleRepository extends JpaRepository<TaskSchedule,Long> {
}
