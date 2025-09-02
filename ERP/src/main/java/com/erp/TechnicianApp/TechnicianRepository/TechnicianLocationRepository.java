package com.erp.TechnicianApp.TechnicianRepository;

import com.erp.TechnicianApp.TechnicianModel.TechnicianLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TechnicianLocationRepository extends JpaRepository<TechnicianLocation, Long> {


    TechnicianLocation findTopByUser_IdOrderByRecordedAtDesc(Long userId);


    TechnicianLocation findTopByUser_IdAndAttendance_IdOrderByRecordedAtDesc(Long userId, Long attendanceId);


    List<TechnicianLocation> findByUser_IdAndRecordedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);


    List<TechnicianLocation> findByUser_IdAndAttendance_Id(Long userId, Long attendanceId);
}
