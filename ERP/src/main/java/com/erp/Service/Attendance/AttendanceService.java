package com.erp.Service.Attendance;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.AttendanceChartResponse;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Dto.Response.AttendanceSummaryChartResponse;
import com.erp.Dto.Response.ResultDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface AttendanceService {

    AttendanceResponse checkIn(Param param);

    AttendanceResponse checkOut(Param param);

    AttendanceResponse updateAttendance(AttendanceRequest request);

    AttendanceResponse getByAttendanceId(Param param);

    List<AttendanceResponse> getByDate(LocalDate date);

    List<AttendanceResponse> getAllAttendances();

    List<AttendanceResponse> getByUserId(Param param);

    List<AttendanceResponse> getMonthlyReport(AttendanceRequest request);

    AttendanceResponse deleteAttendanceByUserIDandDate(AttendanceRequest request);

    AttendanceResponse deleteAllAttendances(AttendanceRequest request);

    int countPresentDaysByUserIdAndMonth(Long userId, YearMonth month);

    void autoCheckout();

    List<AttendanceChartResponse> getMonthlyAttendanceAnalytics(AttendanceRequest request);

    AttendanceSummaryChartResponse getAttendanceSummaryAnalytics(AttendanceRequest request);

    ResultDto<AttendanceResponse> getFilteredAttendance(FilterRequest filterRequest);
}