package com.erp.Service.Attendance;

import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.*;
import com.erp.Enum.AttendanceStatus;
import com.erp.Exception.Attendance.AttendanceAlreadyExistsException;
import com.erp.Exception.Attendance.AttendanceInvalidException;
import com.erp.Exception.Attendance.AttendanceNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.Attendance.AttendanceMapper;
import com.erp.Model.Attendance;
import com.erp.Model.User;
import com.erp.Repository.Attendance.AttendanceRepository;
import com.erp.Repository.User.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Transactional
@Service
@AllArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private static final double FIXED_WORKING_HOURS = 8.0;

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final UserRepository userRepository;
    private final Clock clock;

    @Override
    public AttendanceResponse checkIn(Param param) {
        long userId = param.getUserId();
        LocalDate today = LocalDate.now(clock);
        LocalDateTime now = LocalDateTime.now(clock);

        attendanceRepository.findByUser_IdAndDate(userId, today)
                .ifPresent(a -> {
                    throw new AttendanceAlreadyExistsException("Already checked in today.");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setCheckIn(now);
        attendance.setCheckOut(now.plusHours((long) FIXED_WORKING_HOURS));
        attendance.setDate(today);

        calculateWorkingDetails(attendance);
        attendanceRepository.save(attendance);

        AttendanceResponse response = attendanceMapper.mapToResponse(attendance);
        response.setWorkingHours(formatHours(String.valueOf(attendance.getWorkingHours())));
        response.setWorkingDays(formatDays(String.valueOf(attendance.getWorkingDays())));

        return response;
    }

    @Override
    public AttendanceResponse checkOut(Param param) {
        long userId = param.getUserId();
        LocalDate today = LocalDate.now(clock);
        LocalDateTime now = LocalDateTime.now(clock);

        Attendance attendance = attendanceRepository.findByUser_IdAndDate(userId, today)
                .orElseThrow(() -> new AttendanceNotFoundException("Check-in record not found for today."));

        if (now.isBefore(attendance.getCheckIn())) {
            throw new AttendanceInvalidException("Check-out cannot be before check-in.");
        }

        attendance.setCheckOut(now);
        calculateWorkingDetails(attendance);
        attendanceRepository.save(attendance);

        AttendanceResponse response = attendanceMapper.mapToResponse(attendance);
        response.setWorkingHours(formatHours(String.valueOf(attendance.getWorkingHours())));
        response.setWorkingDays(formatDays(String.valueOf(attendance.getWorkingDays())));

        return response;
    }

    @Override
    public AttendanceResponse updateAttendance(AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findByUser_IdAndDate(request.getUserId(), request.getDate())
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance not found for user and date."));

        if (request.getCheckInTime() != null) {
            attendance.setCheckIn(request.getCheckInTime());
        }

        if (request.getCheckOutTime() != null) {
            if (attendance.getCheckIn() != null && request.getCheckOutTime().isBefore(attendance.getCheckIn())) {
                throw new AttendanceInvalidException("Check-out cannot be before check-in.");
            }
            attendance.setCheckOut(request.getCheckOutTime());
        }

        calculateWorkingDetails(attendance);
        attendanceRepository.save(attendance);

        AttendanceResponse response = attendanceMapper.mapToResponse(attendance);
        response.setWorkingHours(formatHours(String.valueOf(attendance.getWorkingHours())));
        response.setWorkingDays(formatDays(String.valueOf(attendance.getWorkingDays())));

        return response;
    }

    @Override
    public AttendanceResponse getByAttendanceId(Param param) {
        Attendance attendance = attendanceRepository.findById(param.getId())
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance not found."));
        return attendanceMapper.mapToResponse(attendance);
    }

    @Override
    public List<AttendanceResponse> getByDate(LocalDate date) {
        List<Attendance> attendances = attendanceRepository.findByDate(date);
        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException("No attendance records found for date: " + date);
        }
        return attendanceMapper.mapToAttendanceResponse(attendances);
    }

    @Override
    public List<AttendanceResponse> getByUserId(Param param) {
        List<Attendance> attendances = attendanceRepository.findByUser_Id(param.getUserId());
        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException("No attendance records found for the user.");
        }
        return attendanceMapper.mapToAttendanceResponse(attendances);
    }

    @Override
    public List<AttendanceResponse> getAllAttendances() {
        List<Attendance> attendances = attendanceRepository.findAll();
        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException("No attendance records found.");
        }
        return attendanceMapper.mapToAttendanceResponse(attendances);
    }

    @Override
    public List<AttendanceResponse> getMonthlyReport(AttendanceRequest request) {
        if (request.getDate() == null) {
            throw new IllegalArgumentException("Date must be provided for monthly report.");
        }

        YearMonth month = YearMonth.from(request.getDate());
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Attendance> attendances = attendanceRepository.findByUser_IdAndDateBetween(request.getUserId(), start, end);

        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException("No attendance records found for the given month.");
        }

        attendances.sort(Comparator.comparing(a -> a.getCheckIn() != null ? a.getCheckIn() : LocalDateTime.MIN));
        return attendanceMapper.mapToAttendanceResponse(attendances);
    }

    @Override
    public AttendanceResponse deleteAttendanceByUserIDandDate(AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findByUser_IdAndDate(request.getUserId(), request.getDate())
                .orElseThrow(() -> new AttendanceNotFoundException("No attendance found for user and date."));
        attendanceRepository.delete(attendance);
        return attendanceMapper.mapToResponse(attendance);
    }

    @Override
    public AttendanceResponse deleteAllAttendances(AttendanceRequest request) {
        List<Attendance> attendances = attendanceRepository.findByUser_Id(request.getUserId());
        if (attendances.isEmpty()) {
            throw new AttendanceNotFoundException("No attendance records for user.");
        }
        attendanceRepository.deleteAll(attendances);
        return attendanceMapper.mapToResponse(attendances.get(attendances.size() - 1));
    }

    @Override
    public int countPresentDaysByUserIdAndMonth(Long userId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        List<Attendance> attendanceList = attendanceRepository.findByUser_IdAndDateBetween(userId, startDate, endDate);
        List<AttendanceResponse> responseList = attendanceMapper.mapToAttendanceResponse(attendanceList);

        Set<LocalDate> uniquePresentDays = new HashSet<>();
        for (AttendanceResponse response : responseList) {
            if (response.getCheckOut() != null) {
                uniquePresentDays.add(response.getDate());
            }
        }
        return uniquePresentDays.size();
    }

    @Override
    public void autoCheckout() {
        LocalDate today = LocalDate.now(clock);
        List<Attendance> pendingCheckouts = attendanceRepository.findByDateAndCheckOutIsNull(today);

        for (Attendance attendance : pendingCheckouts) {
            if (attendance.getCheckIn() != null) {
                LocalDateTime autoCheckOut = attendance.getCheckIn().plusHours((long) FIXED_WORKING_HOURS);
                attendance.setCheckOut(autoCheckOut);
                attendance.setWorkingHours(FIXED_WORKING_HOURS);
                attendance.setWorkingDays(1.0);
                attendanceRepository.save(attendance);
            }
        }
    }

    private void calculateWorkingDetails(Attendance attendance) {
        double hours = 0.0;
        if (attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
            hours = Duration.between(attendance.getCheckIn(), attendance.getCheckOut()).toMinutes() / 60.0;
        }

        attendance.setWorkingHours(hours);

        if (hours == 0.0) {
            attendance.setWorkingDays(0.0);
        } else if (hours >= FIXED_WORKING_HOURS) {
            attendance.setWorkingDays(1.0);
        } else {
            attendance.setWorkingDays(0.5);
        }
    }

    private String formatHours(String value) {
        double hours = Double.parseDouble(value);
        int h = (int) hours;
        int m = (int) Math.round((hours - h) * 60);

        if (h == 0 && m == 0) return "0 minutes";
        if (h == 0) return m + " minutes";
        if (m == 0) return h + " hours";
        return h + " hours " + m + " minutes";
    }

    private String formatDays(String value) {
        double days = Double.parseDouble(value);
        if (days == 1.0) return "1 day";
        if (days == 0.5) return "Half day";
        return days + " days";
    }

    @Override
    public List<AttendanceChartResponse> getMonthlyAttendanceAnalytics(AttendanceRequest request) {
        final Long userId = request.getUserId();
        final LocalDate fromDate = request.getFromDate();
        final LocalDate toDate = request.getToDate();

        List<Attendance> attendanceList = attendanceRepository.findByUserIdAndDateBetween(userId, fromDate, toDate);

        List<AttendanceChartResponse> responseList = new ArrayList<>();
        LocalDate currentDate = fromDate;

        while (!currentDate.isAfter(toDate)) {
            final LocalDate dateToCheck = currentDate;
            boolean isPresent = attendanceList.stream().anyMatch(a -> a.getDate().isEqual(dateToCheck));
            AttendanceChartResponse response = new AttendanceChartResponse();
            response.setDate(currentDate);
            response.setStatus(isPresent ? AttendanceStatus.PRESENT.name() : AttendanceStatus.LEAVE.name());
            responseList.add(response);
            currentDate = currentDate.plusDays(1);
        }

        return responseList;
    }

    @Override
    public AttendanceSummaryChartResponse getAttendanceSummaryAnalytics(AttendanceRequest request) {
        final Long userId = request.getUserId();
        final LocalDate fromDate = request.getFromDate();
        final LocalDate toDate = request.getToDate();

        List<Attendance> attendanceList = attendanceRepository.findByUserIdAndDateBetween(userId, fromDate, toDate);

        int presentDays = 0;
        int absentDays = 0;

        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            final LocalDate dateToCheck = currentDate;
            boolean isPresent = attendanceList.stream().anyMatch(a -> a.getDate().isEqual(dateToCheck));
            if (isPresent) presentDays++;
            else absentDays++;
            currentDate = currentDate.plusDays(1);
        }

        AttendanceSummaryChartResponse response = new AttendanceSummaryChartResponse();
        response.setPresentDays(presentDays);
        response.setAbsentDays(absentDays);
        return response;
    }
}