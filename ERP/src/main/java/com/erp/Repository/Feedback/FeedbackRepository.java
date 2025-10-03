package com.erp.Repository.Feedback;

import com.erp.Model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback , Long> {

    @Query(value =  "select * from feedbacks where task_id = :taskId and" +
            " customer_id = :customerId and active = true " , nativeQuery = true)
    Feedback findByTaskIdAndCustomerId(long taskId , long customerId);

}
