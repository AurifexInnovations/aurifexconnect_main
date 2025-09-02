package com.erp.TechnicianApp.TechnicianService.TechnicianAttendance;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.AttendanceChartResponse;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Dto.Response.AttendanceSummaryChartResponse;
import com.erp.Service.Helper.AttendanceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianAttendanceServiceImpl implements TechnicianAttendanceService {

    private final AttendanceHelper attendanceHelper;

    @Override
    public AttendanceResponse checkIn(Param param) {
        return attendanceHelper.checkIn(param);
    }

    @Override
    public AttendanceResponse checkOut(Param param) {
        return attendanceHelper.checkOut(param);
    }

    @Override
    public AttendanceResponse updateAttendance(AttendanceRequest request) {
        return attendanceHelper.updateAttendance(request);
    }

    @Override
    public AttendanceResponse getByAttendanceId(Param param) {
        return attendanceHelper.getByAttendanceId(param);
    }

    @Override
    public List<AttendanceResponse> getByUserId(Param param) {
        return attendanceHelper.getByUserId(param);
    }

    @Override
    public List<AttendanceResponse> getMonthlyReport(AttendanceRequest request) {
        return attendanceHelper.getMonthlyReport(request);
    }

    @Override
    public List<AttendanceResponse> getAllAttendances() {
        return attendanceHelper.getAllAttendances();
    }

    @Override
    public void deleteAttendanceByUserIDandDate(AttendanceRequest request) {
        attendanceHelper.deleteAttendanceByUserIDandDate(request);
    }

    @Override
    public AttendanceResponse deleteAllAttendances(AttendanceRequest request) {
        return attendanceHelper.deleteAllAttendances(request);
    }

    @Override
    public List<AttendanceChartResponse> getMonthlyAttendanceAnalytics(AttendanceRequest request) {
        return attendanceHelper.getMonthlyAttendanceAnalytics(request);
    }

    @Override
    public AttendanceSummaryChartResponse getAttendanceSummaryAnalytics(AttendanceRequest request) {
        return attendanceHelper.getAttendanceSummaryAnalytics(request);
    }
}
