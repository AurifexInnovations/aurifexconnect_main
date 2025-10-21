package com.erp.Repository.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUser_IdAndDate(Long userId, LocalDate date);

    List<Attendance> findByUser_Id(Long userId);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByUser_IdAndDateBetween(Long userId,
                                                 LocalDate startDate,
                                                 LocalDate endDate);

    List<Attendance> findByDateAndCheckOutIsNull(LocalDate date);

    Optional<Attendance> findByUserId(Long userId);

    int countByDateAndStatus(LocalDate date, String status);

    List<Attendance> findByDateBetween(LocalDate start, LocalDate end);

    int countByStatus(String status);

    List<Attendance> findByUserIdAndDateBetween(Long userId, LocalDate fromDate, LocalDate toDate);




}