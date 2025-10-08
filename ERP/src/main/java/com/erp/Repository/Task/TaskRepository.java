package com.erp.Repository.Task;

import com.erp.Dto.Response.GetAllTaskResponse;
import com.erp.Enum.TaskStatus;
import com.erp.Model.Task;
import com.erp.Projection.TechnicianResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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






//    @Query(value = """
//    SELECT
//        t.task_id AS taskId,
//        t.task_category AS category,
//        t.created_at AS createdAt,
//        t.updated_at AS updatedAt,
//        s.staff_name AS staffName,
//        s.email AS staffEmail,
//        s.contact_no AS staffPhone,
//        s.designation AS staffDesignation,
//        s.status AS staffStatus
//    FROM task t
//    LEFT JOIN task_services ts ON t.task_id = ts.task_id
//    LEFT JOIN staff s ON ts.service_id = s.id
//    WHERE (:status IS NULL OR t.status = :status)
//      AND (:category IS NULL OR t.task_category = :category)
//      AND (:startDate IS NULL OR t.created_at >= :startDate)
//      AND (:endDate IS NULL OR t.created_at <= :endDate)
//    ORDER BY t.task_id DESC
//    """, nativeQuery = true)
//    List<TechnicianResponse> searchTasksWithStaff(
//            @Param("status") String status,
//            @Param("category") String category,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );
//


//    @Query(value = """
//    SELECT
//        u.id AS id,
//        t.task_category AS category,
//        u.first_name || ' ' || u.last_name AS name,
//        u.email AS email,
//        u.phone_no AS phone,
//        u.designation AS designation,
//        u.status AS status,
//        u.created_at AS createdAt,
//        u.last_modified_at AS updatedAt
//    FROM task t
//    LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
//    LEFT JOIN users u ON tt.technician_id = u.id
//    LEFT JOIN task_schedule ts
//        ON t.task_id = ts.task_id
//        AND (:assignedDateStart IS NULL OR ts.assigned_date BETWEEN :assignedDateStart AND :assignedDateEnd)
//    WHERE (:status IS NULL OR u.status = :status)
//      AND (:category IS NULL OR t.task_category = :category)
//    ORDER BY t.task_id DESC
//    OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
//    """, nativeQuery = true)
//    List<TechnicianResponse> searchTasksWithScheduleAndTechnicians(
//            @Param("status") String status,
//            @Param("category") String category,
//            @Param("assignedDateStart") LocalDate assignedDateStart,
//            @Param("assignedDateEnd") LocalDate assignedDateEnd,
//            @Param("offset") int offset,
//            @Param("limit") int limit
//    );


    @Query(value = """
    SELECT
        u.id AS id,
        t.task_category AS category,
        u.first_name || ' ' || u.last_name AS name,
        u.email AS email,
        u.phone_no AS phone,
        u.designation AS designation,
        CASE
            WHEN u.is_active = TRUE THEN 'Active'
            ELSE 'Inactive'
        END AS status,
        u.created_at AS createdAt,
        u.last_modified_at AS updatedAt
    FROM task t
    LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
    LEFT JOIN users u ON tt.technician_id = u.id
    LEFT JOIN task_schedule ts ON t.task_id = ts.task_id
    WHERE (:status IS NULL OR (u.is_active = TRUE AND :status = 'Active') OR (u.is_active = FALSE AND :status = 'Inactive'))
      AND (:category IS NULL OR t.task_category = :category)
      AND ts.assigned_date BETWEEN :startDate AND :endDate
    ORDER BY t.task_id DESC
""", nativeQuery = true)
    List<TechnicianResponse> searchTasksWithScheduleAndTechnicians(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") Boolean status,
            @Param("category") String category
    );













    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.taskStatus = :status WHERE t.taskId = :taskId")
    int updateTaskStatus(Long taskId, TaskStatus status);




}
