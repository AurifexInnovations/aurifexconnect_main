package com.erp.Repository.Task;

import com.erp.Model.TechnicianTaskMapper;
import com.erp.Projection.TechnitianFeedbackDetailProjection;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TechnicianTaskMapperRepository extends JpaRepository<TechnicianTaskMapper,Long> {

    void deleteByTaskId(Long taskId);

    List<TechnicianTaskMapper> getTechnitiansByTaskId(long taskId);

    @Modifying
    @Transactional
    @Query(value =  "update task_technicians set feedback_id = :feedBackId where task_id = :taskId" , nativeQuery = true)
    Integer  updateTechnitianFeedBackDetails(long taskId , long feedBackId);

    @Query(value = "select u.first_name as firstName, u.last_name  as lastName , " +
            " tt.technician_id as technicianId " +
            " from task_technicians as tt " +
            " inner join users u " +
            " on u.id = tt.technician_id " +
            " where tt.feedback_id =:feedbackId and u.is_active =true " , nativeQuery = true)
    List<TechnitianFeedbackDetailProjection> getTechnitianFeedbackDetails(long feedbackId);

    List<TechnicianTaskMapper> findByTaskId(long taskId);


}