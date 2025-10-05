package com.erp.Repository.Task;

import com.erp.Model.TaskMaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskMaterialRepository extends JpaRepository<TaskMaterial, Long> {

    void deleteByTaskId(Long taskId);

    @Transactional
    @Modifying
    @Query("UPDATE TaskMaterial tm SET tm.isUsed = true WHERE tm.taskId = :taskId AND tm.materialId = :materialId")
    int updateTaskMaterialAgienstTaskIdAndMaterialID(@Param("taskId") Long taskId, @Param("materialId") Long materialId);

    List<TaskMaterial> findByTaskId(Long taskId);
}

