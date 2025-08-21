package com.erp.TechnicianApp.Repository.TechnicianAttendance;


import com.erp.TechnicianApp.Model.TechnicianAttendance.TechnicianAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TechnicianAttendanceRepository extends JpaRepository<TechnicianAttendance, Long> {

    // Fetch today's attendance for a technician (for check-in/check-out logic)
    @Query("SELECT ta FROM TechnicianAttendance ta " +
            "WHERE ta.technician.technicianId = :technicianId " +
            "AND DATE(ta.checkInTime) = :date")
    Optional<TechnicianAttendance> findByTechnician_TechnicianIdAndAttendanceDate(
            @Param("technicianId") Long technicianId,
            @Param("date") LocalDate date
    );

    // Fetch all attendance records for a technician in a given month (for HR monthly view)
    @Query("SELECT ta FROM TechnicianAttendance ta " +
            "WHERE ta.technician.technicianId = :technicianId " +
            "AND DATE(ta.checkInTime) BETWEEN :startDate AND :endDate")
    List<TechnicianAttendance> findByTechnician_TechnicianIdAndAttendanceDateBetween(
            @Param("technicianId") Long technicianId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Fetch all attendance records in a month for HR to view all technicians
    @Query("SELECT ta FROM TechnicianAttendance ta " +
            "WHERE DATE(ta.checkInTime) BETWEEN :startDate AND :endDate")
    List<TechnicianAttendance> findByAttendanceDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );



    @Query("SELECT a FROM TechnicianAttendance a " +
            "WHERE a.technician.id = :technicianId " +
            "AND MONTH(a.checkInTime) = :month " +
            "AND YEAR(a.checkInTime) = :year")
    List<TechnicianAttendance> findByTechnicianIdAndMonthAndYear(
            @Param("technicianId") Long technicianId,
            @Param("month") int month,
            @Param("year") int year);

}
