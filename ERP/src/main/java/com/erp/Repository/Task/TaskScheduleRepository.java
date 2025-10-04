package com.erp.Repository.Task;

import com.erp.Model.TaskSchedule;
import com.erp.Projection.TechnicianResponse;
import com.erp.Projection.TechnicianTaskProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskScheduleRepository extends JpaRepository<TaskSchedule,Long> {

    @Query(value = """
        SELECT 
            u.first_name || ' ' || u.last_name AS technicianName,
            ts.service_location AS attendanceLocation,
            t.task_name AS taskName
        FROM task t
        LEFT JOIN task_schedule ts ON t.task_id = ts.task_id
        LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
        LEFT JOIN users u ON tt.technician_id = u.id
        WHERE (:assignedDate IS NULL OR ts.assigned_date = :assignedDate)
          AND (:technicianId IS NULL OR u.id = :technicianId)
        ORDER BY t.created_at DESC
        LIMIT :size OFFSET :offset
        """, nativeQuery = true)
    List<TechnicianTaskProjection> getTechnicianTasks(
            @Param("assignedDate") LocalDate localDateassignedDate,
            @Param("technicianId") Long technicianId,
            @Param("size") int size,
            @Param("offset") int offset
    );

}
