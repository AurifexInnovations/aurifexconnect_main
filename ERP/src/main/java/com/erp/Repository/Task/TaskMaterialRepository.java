package com.erp.Repository.Task;

import com.erp.Model.TaskMaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMaterialRepository extends JpaRepository<TaskMaterial, Long> {

    void deleteByTaskId(Long taskId);
}
