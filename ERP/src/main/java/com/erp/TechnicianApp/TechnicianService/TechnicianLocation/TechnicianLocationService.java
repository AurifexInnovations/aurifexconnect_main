package com.erp.TechnicianApp.TechnicianService.TechnicianLocation;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationImageRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianLocationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TechnicianLocationService {
    TechnicianLocationResponse pushLocation(TechnicianLocationRequest request);
    TechnicianLocationResponse pushLocationWithImage(TechnicianLocationImageRequest request);
    TechnicianLocationResponse getLatest(Long userId);
    TechnicianLocationResponse getLatestForAttendance(Long userId, Long attendanceId); // ✅ new
    List<TechnicianLocationResponse> getHistory(Long userId, LocalDateTime start, LocalDateTime end);
    List<TechnicianLocationResponse> getHistoryForAttendance(Long userId, Long attendanceId); // ✅ new
    List<TechnicianLocationResponse> getAll();
}
