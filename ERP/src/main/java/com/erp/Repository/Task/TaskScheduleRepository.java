package com.erp.Repository.Task;

import com.erp.Dto.Response.TechnicianPerformanceDTO;
import com.erp.Model.TaskSchedule;
import com.erp.Projection.*;
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
    WHERE ts.assigned_date = COALESCE(:assignedDate, ts.assigned_date)
      AND u.id = COALESCE(:technicianId, u.id)
    ORDER BY t.created_at DESC
    LIMIT :size OFFSET :offset
    """, nativeQuery = true)
    List<TechnicianTaskProjection> getTechnicianTasks(
            @Param("assignedDate") LocalDate assignedDate,
            @Param("technicianId") Long technicianId,
            @Param("size") int size,
            @Param("offset") int offset
    );



    @Query(value = """
        SELECT 
            RANK() OVER (ORDER BY ROUND(AVG(sub.rating)::numeric, 2) DESC) AS rank,
            tt.technician_id AS technicianId,
            CONCAT(u.first_name, ' ', u.last_name) AS technicianName,
            COUNT(DISTINCT t.task_id) AS completedTasks,
            ROUND(AVG(sub.rating)::numeric, 2) AS avgRating
        FROM task_schedule AS ts
        INNER JOIN task AS t 
            ON t.task_id = ts.task_id
        INNER JOIN task_technicians AS tt 
            ON tt.task_id = ts.task_id
        INNER JOIN users AS u 
            ON u.id = tt.technician_id
        LEFT JOIN (
            SELECT DISTINCT
                tt_inner.technician_id,
                f.rating,
                f.id AS feedback_id
            FROM task_technicians AS tt_inner
            LEFT JOIN feedbacks AS f
                ON f.id = tt_inner.feedback_id AND f.is_active = TRUE
        ) AS sub
            ON sub.technician_id = tt.technician_id
        WHERE 
            t.status = 'COMPLETED'
            AND ts.assigned_date BETWEEN :startDate AND :endDate
        GROUP BY 
            tt.technician_id, technicianName
        ORDER BY rank
        """, nativeQuery = true)
    List<TechnicianLeaderboardProjection> findTechnicianLeaderboard(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );



    @Query(value = """
        SELECT 
            tt.technician_id AS technicianId,
            i.item_id AS productId,
            i.item_name AS productName,
            SUM(tm.quantity) AS totalQuantity,
            tm.unit AS unit
        FROM task_material AS tm
        INNER JOIN inventory AS i 
            ON i.item_id = tm.material_id
        INNER JOIN task_technicians AS tt 
            ON tt.task_id = tm.task_id
        INNER JOIN task_schedule AS ts
            ON ts.task_id = tm.task_id
        WHERE 
            tm.is_used = TRUE
            AND ts.assigned_date BETWEEN :startDate AND :endDate
        GROUP BY 
            tt.technician_id, i.item_id, i.item_name, tm.unit
        ORDER BY tt.technician_id, i.item_name
        """, nativeQuery = true)
    List<TechnicianMaterialProjection> findTechnicianMaterialUsage(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    TaskSchedule findByTaskId(Long taskId);



}
