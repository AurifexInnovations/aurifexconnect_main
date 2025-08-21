package com.erp.TechnicianApp.Service.TechnicianAttendance;



import com.erp.TechnicianApp.Dto.Response.MonthlyAttendanceSummaryResponse;
import com.erp.TechnicianApp.Dto.Response.TechnicianAttendanceResponse;
import com.erp.TechnicianApp.Enum.AttendanceStatus;
import com.erp.TechnicianApp.Enum.TechnicianDayStatus;
import com.erp.TechnicianApp.Mapper.TechnicianAttendance.TechnicianAttendanceMapper;
import com.erp.TechnicianApp.Model.Task.Task;
import com.erp.TechnicianApp.Model.Technician.Technician;
import com.erp.TechnicianApp.Model.TechnicianAttendance.TechnicianAttendance;
import com.erp.TechnicianApp.Repository.Task.TaskRepository;
import com.erp.TechnicianApp.Repository.Technician.TechnicianRepository;
import com.erp.TechnicianApp.Repository.TechnicianAttendance.TechnicianAttendanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TechnicianAttendanceServiceImpl implements TechnicianAttendanceService {

    private final TechnicianAttendanceRepository attendanceRepository;
    private final TechnicianRepository technicianRepository;
    private final TaskRepository taskRepository;
    private final TechnicianAttendanceMapper mapper;

    @Override
    public TechnicianAttendanceResponse checkIn(Long technicianId, Long taskId) {
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new EntityNotFoundException("Technician not found"));

        Task task = null;
        if (taskId != null) {
            task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        }

        LocalDate today = LocalDate.now();

        Optional<TechnicianAttendance> optionalAttendance =
                attendanceRepository.findByTechnician_TechnicianIdAndAttendanceDate(technicianId, today);

        TechnicianAttendance attendance;
        if (optionalAttendance.isPresent()) {
            attendance = optionalAttendance.get();
            if (attendance.getCheckInTime() == null) {
                attendance.setCheckInTime(LocalDateTime.now());
                attendance.setStatus(AttendanceStatus.CHECK_IN);
            }
        } else {
            attendance = new TechnicianAttendance();
            attendance.setTechnician(technician);
            attendance.setTask(task);
            attendance.setDayStatus(TechnicianDayStatus.PRESENT);
            attendance.setStatus(AttendanceStatus.CHECK_IN);
            attendance.setCheckInTime(LocalDateTime.now());
        }

        attendanceRepository.save(attendance);
        return mapper.mapToResponse(attendance);
    }

    @Override
    public TechnicianAttendanceResponse checkOut(Long technicianId, Long taskId) {
        LocalDate today = LocalDate.now();

        TechnicianAttendance attendance = attendanceRepository
                .findByTechnician_TechnicianIdAndAttendanceDate(technicianId, today)
                .orElseThrow(() -> new EntityNotFoundException("No check-in found for today"));

        attendance.setCheckOutTime(LocalDateTime.now());
        attendance.setStatus(AttendanceStatus.CHECK_OUT);

        attendanceRepository.save(attendance);
        return mapper.mapToResponse(attendance);
    }

    @Override
    public TechnicianAttendanceResponse getDailyAttendance(Long technicianId, LocalDate date) {
        TechnicianAttendance attendance = attendanceRepository
                .findByTechnician_TechnicianIdAndAttendanceDate(technicianId, date)
                .orElseThrow(() -> new EntityNotFoundException("No attendance found for given date"));

        return mapper.mapToResponse(attendance);
    }

    @Override
    public List<MonthlyAttendanceSummaryResponse> getAllTechniciansMonthlyAttendance(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Fetch all records of all technicians for the month
        List<TechnicianAttendance> records =
                attendanceRepository.findByAttendanceDateBetween(startDate, endDate);

        // Group by Technician
        Map<Long, List<TechnicianAttendance>> technicianWiseRecords =
                records.stream().collect(Collectors.groupingBy(ta -> ta.getTechnician().getTechnicianId()));

        List<MonthlyAttendanceSummaryResponse> summaries = new ArrayList<>();

        for (Map.Entry<Long, List<TechnicianAttendance>> entry : technicianWiseRecords.entrySet()) {
            Long technicianId = entry.getKey();
            Technician technician = entry.getValue().get(0).getTechnician();
            List<TechnicianAttendance> technicianRecords = entry.getValue();

            int presentDays = (int) technicianRecords.stream()
                    .filter(ta -> ta.getDayStatus() == TechnicianDayStatus.PRESENT)
                    .count();

            int absentDays = (int) technicianRecords.stream()
                    .filter(ta -> ta.getDayStatus() == TechnicianDayStatus.ABSENT)
                    .count();

            int leaveDays = (int) technicianRecords.stream()
                    .filter(ta -> ta.getDayStatus() == TechnicianDayStatus.LEAVE)
                    .count();

            int holidayDays = (int) technicianRecords.stream()
                    .filter(ta -> ta.getDayStatus() == TechnicianDayStatus.HOLIDAY)
                    .count();


            int totalDays = startDate.lengthOfMonth();

            MonthlyAttendanceSummaryResponse summary = MonthlyAttendanceSummaryResponse.builder()
                    .technicianId(technicianId)
                    .technicianName(technician.getTechnicianName())
                    .totalWorkingDays(totalDays)
                    .presentDays(presentDays)
                    .absentDays(absentDays)
                    .leaveDays(leaveDays)
                    .holidayDays(holidayDays)
                    .month(YearMonth.of(year, month).getMonth().name() + " " + year)
                    .build();


            summaries.add(summary);
        }

        return summaries;
    }

    @Override
    public MonthlyAttendanceSummaryResponse getMonthlyAttendanceSummary(Long technicianId, int month, int year) {
        // Fetch all attendance records for that technician & month
        List<TechnicianAttendance> records = attendanceRepository.findByTechnicianIdAndMonthAndYear(technicianId, month, year);

        int totalDays = records.size();
        int presentDays = (int) records.stream().filter(r -> r.getDayStatus() == TechnicianDayStatus.PRESENT).count();
        int absentDays = (int) records.stream().filter(r -> r.getDayStatus() == TechnicianDayStatus.ABSENT).count();
        int leaveDays = (int) records.stream().filter(r -> r.getDayStatus() == TechnicianDayStatus.LEAVE).count();
        int holidayDays = (int) records.stream().filter(r -> r.getDayStatus() == TechnicianDayStatus.HOLIDAY).count();

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        String monthName = YearMonth.of(year, month).getMonth()
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);

        return MonthlyAttendanceSummaryResponse.builder()
                .technicianId(technicianId)
                .technicianName(technician.getTechnicianName())
                .totalWorkingDays(totalDays)
                .presentDays(presentDays)
                .absentDays(absentDays)
                .leaveDays(leaveDays)
                .holidayDays(holidayDays)
                .month(monthName + " " + year) // Example: "August 2025"
                .build();
    }
}
