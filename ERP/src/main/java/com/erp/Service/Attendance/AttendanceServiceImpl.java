package com.erp.Service.Attendance;

import com.erp.CustomRepository.AttendanceCustomRepository;
import com.erp.Dto.Request.AttendanceRequest;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.*;
import com.erp.Enum.AttendanceStatus;
import com.erp.Exception.Attendance.AttendanceAlreadyExistsException;
import com.erp.Exception.Attendance.AttendanceInvalidException;
import com.erp.Exception.Attendance.AttendanceNotFoundException;
import com.erp.Exception.ResourceFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.Attendance.AttendanceMapper;
import com.erp.Model.Attendance;
import com.erp.Model.User;
import com.erp.Repository.Attendance.AttendanceRepository;
import com.erp.Repository.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private static final double FIXED_WORKING_HOURS = 8.0;

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final UserRepository userRepository;

    private final  AttendanceCustomRepository attendanceCustomRepository;
    private final Clock clock;

    @Override
    public AttendanceResponse checkIn(Param param)
    {
        long userId = param.getUserId();
        LocalDate today = LocalDate.now(clock);
        LocalDateTime now = LocalDateTime.now(clock);

        attendanceRepository.findByUser_IdAndDate(userId, today)
                .ifPresent(a -> {
                    throw new ResourceFoundException("Already checked in today.");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        System.out.println("**********************************"+userId);

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setCheckIn(now);
        attendance.setCheckOut(now.plusHours((long) FIXED_WORKING_HOURS));
        attendance.setDate(today);
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance.setUsername(user.getFirstName() +" "+user.getLastName());

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
    @Transactional
    public AttendanceResponse updateAttendance(AttendanceRequest request) {
        if (request == null || request.getUserId() <= 0 || request.getDate() == null) {
            throw new IllegalArgumentException("User ID and date must be provided.");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Attendance attendance;
        if (request.getId() > 0) {
            attendance = attendanceRepository.findById(request.getId())
                    .orElseThrow(() -> new AttendanceNotFoundException("Attendance not found for id: " + request.getId()));
            if (attendance.getUser().getId() != request.getUserId() || !attendance.getDate().equals(request.getDate())) {
                throw new AttendanceInvalidException("User ID or date does not match attendance record.");
            }
        } else {
            attendance = attendanceRepository.findByUser_IdAndDate(request.getUserId(), request.getDate())
                    .orElseGet(() -> {
                        Attendance newAttendance = new Attendance();
                        newAttendance.setUser(user);
                        newAttendance.setDate(request.getDate());
                        newAttendance.setStatus(AttendanceStatus.PRESENT);
                        return newAttendance;
                    });
        }

        if (request.getCheckInTime() != null) {
            if (!request.getCheckInTime().toLocalDate().equals(request.getDate())) {
                throw new AttendanceInvalidException("Check-in time must match the specified date.");
            }
            attendance.setCheckIn(request.getCheckInTime());
        }
        if (request.getCheckOutTime() != null) {
            if (attendance.getCheckIn() == null) {
                throw new AttendanceInvalidException("Check-in time must be set before check-out.");
            }
            if (!request.getCheckOutTime().toLocalDate().equals(request.getDate())) {
                throw new AttendanceInvalidException("Check-out time must match the specified date.");
            }
            if (request.getCheckOutTime().isBefore(attendance.getCheckIn())) {
                throw new AttendanceInvalidException("Check-out time cannot be before check-in.");
            }
            attendance.setCheckOut(request.getCheckOutTime());
        }

        if (request.getStatus() != null) {
            try {
                attendance.setStatus(AttendanceStatus.valueOf(request.getStatus()));
            } catch (IllegalArgumentException e) {
                throw new AttendanceInvalidException("Invalid attendance status: " + request.getStatus());
            }
        }

        calculateWorkingDetails(attendance);
        Attendance savedAttendance = attendanceRepository.save(attendance);
        AttendanceResponse response = attendanceMapper.mapToResponse(savedAttendance);
        response.setWorkingHours(formatHours(String.valueOf(savedAttendance.getWorkingHours())));
        response.setWorkingDays(formatDays(String.valueOf(savedAttendance.getWorkingDays())));
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
    public ResultDto<AttendanceResponse> getAllAttendances() {
        List<AttendanceResponse> attendances = attendanceMapper.mapToAttendanceResponse(attendanceRepository.findAll());

        ResultDto<AttendanceResponse> resultDto = new ResultDto<>();

        resultDto.setResults(attendances != null ? attendances : List.of());
        resultDto.setCount(attendances != null ? attendances.size() : 0);

        return resultDto;
    }

    @Override
    public List<AttendanceResponse> getMonthlyReport(AttendanceRequest request) {
        if (request.getUserId() <= 0 || request.getMonth() == null)
            throw new IllegalArgumentException("User ID and month are required.");

        YearMonth month = YearMonth.parse(request.getMonth());
        LocalDate start = month.atDay(1), end = month.atEndOfMonth();

        List<Attendance> records = attendanceRepository.findByUser_IdAndDateBetween(request.getUserId(), start, end);
        if (records.isEmpty()) throw new AttendanceNotFoundException("No records found for " + month);
        return attendanceMapper.mapToAttendanceResponse(records);
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
        Long userId = request.getUserId();
        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();
        List<Attendance> attendanceList = attendanceRepository.findByUserIdAndDateBetween(userId, fromDate, toDate);
        List<AttendanceChartResponse> responseList = new ArrayList<>();

        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            AttendanceChartResponse response = new AttendanceChartResponse();
            response.setDate(currentDate);
            Attendance matchingAttendance = null;
            for (Attendance attendance : attendanceList) {
                if (attendance.getDate().equals(currentDate)) {
                    matchingAttendance = attendance;
                    break;
                }
            }

            if (matchingAttendance != null) {
                if (matchingAttendance.getStatus() != null) {
                    response.setStatus(matchingAttendance.getStatus().name());
                } else {
                    response.setStatus(AttendanceStatus.PRESENT.name()); // fallback
                }
            } else {
                response.setStatus(AttendanceStatus.ABSENT.name());
            }
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


    @Override
    public ResultDto<AttendanceResponse> getFilteredAttendance(FilterRequest filterRequest) {
        log.info("Into [AttendanceServiceImpl] [getAttendanceDetails]");

        ResultDto<AttendanceResponse> attendanceResponses = new ResultDto<>();

        try {
            attendanceResponses = attendanceCustomRepository.getFilteredAttendance(filterRequest);
        } catch (Exception exception) {
            log.error("Error [AttendanceServiceImpl] [getAttendanceDetails] :: {} :: {}", exception.getMessage(), exception);
        }

        log.info("Exit [AttendanceServiceImpl] [getAttendanceDetails]");
        return attendanceResponses;
    }
    @Override
    @Transactional
    public AttendanceResponse createAttendance(AttendanceRequest request) {
        long userId = request.getUserId();
        LocalDate today = request.getDate();
        LocalDateTime now = LocalDateTime.now(clock);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        attendanceRepository.findByUser_IdAndDate(userId, today)
                .ifPresent(a -> {
                    throw new ResourceFoundException("Already checked in today.");
                });

        System.out.println("**********************************"+userId);

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setCheckIn(request.getCheckInTime());
        attendance.setCheckOut(request.getCheckOutTime());
        attendance.setDate(today);
        attendance.setStatus(AttendanceStatus.valueOf(request.getStatus()));
        attendance.setUsername(user.getFirstName() +" "+user.getLastName());

        calculateWorkingDetails(attendance);
        attendanceRepository.save(attendance);

        AttendanceResponse response = attendanceMapper.mapToResponse(attendance);
        response.setWorkingHours(formatHours(String.valueOf(attendance.getWorkingHours())));
        response.setWorkingDays(formatDays(String.valueOf(attendance.getWorkingDays())));

        return response;
    }

}
