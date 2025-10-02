package com.erp.Repository.Task;

import com.erp.Dto.Response.GetAllTaskResponse;
import com.erp.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {


    @Query(value = """
            SELECT t.task_id as taskId,
                   t.task_name as taskName,
                   t.status as status,
                   s.assigned_date as assignedDate,
                   s.service_location as serviceLocation
            FROM task t
            LEFT JOIN task_schedule s ON t.task_id = s.task_id
            ORDER BY t.task_id DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<GetAllTaskResponse> findTasksWithSchedule(@Param("limit") int limit, @Param("offset") int offset);


}
