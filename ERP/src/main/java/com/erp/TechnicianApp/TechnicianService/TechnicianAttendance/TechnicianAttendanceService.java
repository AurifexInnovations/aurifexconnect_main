package com.erp.TechnicianApp.TechnicianService.TechnicianAttendance;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.AttendanceChartResponse;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Dto.Response.AttendanceSummaryChartResponse;
import java.util.List;

public interface TechnicianAttendanceService {

    AttendanceResponse checkIn(Param param);

    AttendanceResponse checkOut(Param param);

    AttendanceResponse updateAttendance(AttendanceRequest request);

    AttendanceResponse getByAttendanceId(Param param);

    List<AttendanceResponse> getByUserId(Param param);

    List<AttendanceResponse> getMonthlyReport(AttendanceRequest request);

    List<AttendanceResponse> getAllAttendances();

    void deleteAttendanceByUserIDandDate(AttendanceRequest request);

    AttendanceResponse deleteAllAttendances(AttendanceRequest request);

    List<AttendanceChartResponse> getMonthlyAttendanceAnalytics(AttendanceRequest request);

    AttendanceSummaryChartResponse getAttendanceSummaryAnalytics(AttendanceRequest request);
}
