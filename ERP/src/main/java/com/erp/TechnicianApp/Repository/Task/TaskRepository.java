package com.erp.TechnicianApp.Repository.Task;

import com.erp.TechnicianApp.Model.Task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTechnician_TechnicianId(Long technicianId);

    List<Task> findByTaskIdOrClientName(long id, String name);
}
