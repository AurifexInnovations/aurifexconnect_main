package com.erp.Repository.Task;

import com.erp.Dto.Response.TechnicianPerformanceDTO;
import com.erp.Model.TaskSchedule;
import com.erp.Projection.LeaderboardProjection;
import com.erp.Projection.TechnicianPerformanceProjection;
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



//    @Query(value = """
//    SELECT u.id as technicianId,
//           CONCAT(u.first_name, ' ', u.last_name) as name,
//           lb.tasks_completed as tasksCompleted,
//           lb.average_rating as averageRating,
//           0 as rank,
//           m.item_name as productName,
//           tm.unit as unit
//    FROM task_schedule ts
//    JOIN technician_task_mapper tt ON ts.task_id = tt.task_id
//    JOIN user u ON tt.technician_id = u.id
//    LEFT JOIN leaderboard lb ON u.id = lb.technician_id
//    LEFT JOIN task_material tm ON ts.task_id = tm.task_id
//    LEFT JOIN inventory m ON tm.material_id = m.id
//    WHERE ts.assigned_date BETWEEN :startDate AND :endDate
//""", nativeQuery = true)
//    List<TechnicianPerformanceDTO> getTechnicianPerformance(
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );


    @Query(value = """
        SELECT 
            u.id AS technicianId,
            CONCAT(u.first_name, ' ', u.last_name) AS name,
            COUNT(ts.id) AS tasksCompleted,
            SUM(CASE WHEN ts.completed_time > ts.assigned_time THEN 1 ELSE 0 END) AS lateMarks,
            COALESCE(AVG(lb.average_rating),0) AS averageRating,
            m.item_name AS productName,
            tm.quantity AS quantity,
            tm.unit AS unit
        FROM task_schedule ts
        JOIN technician_task_mapper tt ON ts.task_id = tt.task_id
        JOIN user u ON tt.technician_id = u.id
        LEFT JOIN leaderboard lb ON u.id = lb.technician_id
        LEFT JOIN task_material tm ON ts.task_id = tm.task_id
        LEFT JOIN inventory m ON tm.material_id = m.id
        WHERE ts.assigned_date BETWEEN :startDate AND :endDate
        GROUP BY u.id, m.item_name, tm.quantity, tm.unit, lb.average_rating
    """, nativeQuery = true)
    List<TechnicianPerformanceProjection> getTechnicianPerformance(@Param("startDate") LocalDate startDate,
                                                                   @Param("endDate") LocalDate endDate);

    @Query(value = """
        SELECT 
            RANK() OVER (ORDER BY lb.average_rating DESC) AS rank,
            u.id AS technicianId,
            CONCAT(u.first_name, ' ', u.last_name) AS name,
            lb.average_rating AS averageRating
        FROM leaderboard lb
        JOIN user u ON lb.technician_id = u.id
    """, nativeQuery = true)
    List<LeaderboardProjection> getLeaderboard();




}
