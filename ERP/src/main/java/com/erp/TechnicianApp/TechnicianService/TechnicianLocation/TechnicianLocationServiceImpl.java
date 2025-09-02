package com.erp.TechnicianApp.TechnicianService.TechnicianLocation;

import com.erp.Repository.User.UserRepository;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationImageRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianLocationResponse;
import com.erp.TechnicianApp.TechnicianMapper.TechnicianLocation.TechnicianLocationMapper;
import com.erp.TechnicianApp.TechnicianModel.TechnicianLocation;
import com.erp.TechnicianApp.TechnicianModel.TechnicianTask;
import com.erp.Model.User;
import com.erp.Model.Attendance;
import com.erp.TechnicianApp.TechnicianRepository.TechnicianLocationRepository;
import com.erp.TechnicianApp.TechnicianRepository.TechnicianTaskRepository;
import com.erp.Repository.Attendance.AttendanceRepository;
import com.erp.TechnicianApp.TechnicianService.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianLocationServiceImpl implements TechnicianLocationService {

    private final TechnicianLocationRepository locationRepository;
    private final UserRepository userRepository;
    private final TechnicianTaskRepository taskRepository;
    private final AttendanceRepository attendanceRepository;
    private final FileStorageService fileStorageService;
    private final TechnicianLocationMapper mapper;

    @Override
    public TechnicianLocationResponse pushLocation(TechnicianLocationRequest request) {
        TechnicianLocation location = mapper.toEntity(request);

        // validate user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        location.setUser(user);

        // optional: link task
        if (request.getTaskId() != null) {
            TechnicianTask task = taskRepository.findById(request.getTaskId()).orElse(null);
            location.setTask(task);
        }

        // optional: link attendance
        if (request.getAttendanceId() != null) {
            Attendance attendance = attendanceRepository.findById(request.getAttendanceId()).orElse(null);
            location.setAttendance(attendance);
        }

        // set timestamp if not provided
        if (location.getRecordedAt() == null) {
            location.setRecordedAt(LocalDateTime.now());
        }

        return mapper.toResponse(locationRepository.save(location));
    }

    @Override
    public TechnicianLocationResponse pushLocationWithImage(TechnicianLocationImageRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TechnicianTask task = null;
        if (request.getTaskId() != null) {
            task = taskRepository.findById(request.getTaskId()).orElse(null);
        }

        String imageUrl = fileStorageService.saveFile(request.getImage());

        TechnicianLocation location = new TechnicianLocation();
        location.setUser(user);
        location.setTask(task);
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setNote(request.getNote());
        location.setImageUrl(imageUrl);
        location.setRecordedAt(LocalDateTime.now());

        return mapper.toResponse(locationRepository.save(location));
    }

    @Override
    public TechnicianLocationResponse getLatest(Long userId) {
        TechnicianLocation latest = locationRepository.findTopByUser_IdOrderByRecordedAtDesc(userId);
        return (latest != null) ? mapper.toResponse(latest) : null;
    }

    @Override
    public TechnicianLocationResponse getLatestForAttendance(Long userId, Long attendanceId) {
        TechnicianLocation latest =
                locationRepository.findTopByUser_IdAndAttendance_IdOrderByRecordedAtDesc(userId, attendanceId);
        return (latest != null) ? mapper.toResponse(latest) : null;
    }

    @Override
    public List<TechnicianLocationResponse> getHistory(Long userId, LocalDateTime start, LocalDateTime end) {
        List<TechnicianLocation> history =
                locationRepository.findByUser_IdAndRecordedAtBetween(userId, start, end);
        return mapper.toResponseList(history);
    }

    @Override
    public List<TechnicianLocationResponse> getHistoryForAttendance(Long userId, Long attendanceId) {
        List<TechnicianLocation> history =
                locationRepository.findByUser_IdAndAttendance_Id(userId, attendanceId);
        return mapper.toResponseList(history);
    }

    @Override
    public List<TechnicianLocationResponse> getAll() {
        return mapper.toResponseList(locationRepository.findAll());
    }
}
