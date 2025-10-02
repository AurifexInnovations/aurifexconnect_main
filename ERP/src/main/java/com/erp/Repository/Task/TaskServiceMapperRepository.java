package com.erp.Repository.Task;

import com.erp.Model.TaskServiceMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface TaskServiceMapperRepository extends JpaRepository<TaskServiceMapper,Long> {

    List<TaskServiceMapper> findByTaskId(Long taskId);

    @Modifying
    @Transactional
    void deleteByTaskId(Long taskId);
}
