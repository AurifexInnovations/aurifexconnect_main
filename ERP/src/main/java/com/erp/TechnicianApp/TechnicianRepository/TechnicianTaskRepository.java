package com.erp.TechnicianApp.TechnicianRepository;

import com.erp.TechnicianApp.TechnicianModel.TechnicianTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnicianTaskRepository extends JpaRepository<TechnicianTask, Long> {
    List<TechnicianTask> findByUserId(Long userId);
}
