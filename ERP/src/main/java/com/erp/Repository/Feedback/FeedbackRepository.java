package com.erp.Repository.Feedback;

import com.erp.Dto.Request.TechnicianStatsDTO;
import com.erp.Model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback , Long> {

    @Query(value =  "select * from feedbacks where task_id = :taskId and" +
            " customer_id = :customerId and active = true " , nativeQuery = true)
    Feedback findByTaskIdAndCustomerId(long taskId , long customerId);

    @Query(value = "SELECT f.technician_id as technicianId, COUNT(t.task_id) as tasksCompleted, AVG(f.rating) as avgRating " +
            "FROM feedback f " +
            "JOIN task t ON f.task_id = t.task_id " +
            "WHERE t.task_status = 'COMPLETED' " +
            "GROUP BY f.technician_id", nativeQuery = true)
    List<TechnicianStatsDTO> getTechnicianStats();




}
