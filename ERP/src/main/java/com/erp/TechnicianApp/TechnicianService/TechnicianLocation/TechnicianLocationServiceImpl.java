package com.erp.TechnicianApp.TechnicianService.TechnicianLocation;

import com.erp.Model.Attendance;
import com.erp.Model.User;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationImageRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianLocationResponse;
import com.erp.TechnicianApp.TechnicianException.Location.TechnicianLocationNotFoundException;
import com.erp.TechnicianApp.TechnicianMapper.TechnicianLocation.TechnicianLocationMapper;
import com.erp.TechnicianApp.TechnicianModel.TechnicianLocation;
import com.erp.TechnicianApp.TechnicianModel.TechnicianTask;
import com.erp.TechnicianApp.TechnicianRepository.TechnicianLocationRepository;
import com.erp.TechnicianApp.TechnicianRepository.TechnicianTaskRepository;
import com.erp.Repository.Attendance.AttendanceRepository;
import com.erp.Repository.User.UserRepository;
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

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new TechnicianLocationNotFoundException("User not found"));

        TechnicianLocation location = mapper.toEntity(request);
        location.setUser(user);

        if (request.getTaskId() != null) {
            TechnicianTask task = taskRepository.findById(request.getTaskId()).orElse(null);
            location.setTask(task);
        }

        if (request.getAttendanceId() != null) {
            Attendance attendance = attendanceRepository.findById(request.getAttendanceId()).orElse(null);
            location.setAttendance(attendance);
        }

        if (location.getRecordedAt() == null) {
            location.setRecordedAt(LocalDateTime.now());
        }

        return mapper.toResponse(locationRepository.save(location));
    }

    @Override
    public TechnicianLocationResponse pushLocationWithImage(TechnicianLocationImageRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new TechnicianLocationNotFoundException("User not found"));

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
        if (latest == null)
            throw new TechnicianLocationNotFoundException("No location found for user ID " + userId);
        return mapper.toResponse(latest);
    }

    @Override
    public TechnicianLocationResponse getLatestForAttendance(Long userId, Long attendanceId) {
        TechnicianLocation latest =
                locationRepository.findTopByUser_IdAndAttendance_IdOrderByRecordedAtDesc(userId, attendanceId);
        if (latest == null)
            throw new TechnicianLocationNotFoundException(
                    "No location found for user ID " + userId + " and attendance ID " + attendanceId);
        return mapper.toResponse(latest);
    }

    @Override
    public List<TechnicianLocationResponse> getHistory(Long userId, LocalDateTime start, LocalDateTime end) {
        List<TechnicianLocation> history =
                locationRepository.findByUser_IdAndRecordedAtBetween(userId, start, end);
        if (history.isEmpty())
            throw new TechnicianLocationNotFoundException(
                    "No location history found for user ID " + userId + " in the given range");
        return mapper.toResponseList(history);
    }

    @Override
    public List<TechnicianLocationResponse> getHistoryForAttendance(Long userId, Long attendanceId) {
        List<TechnicianLocation> history =
                locationRepository.findByUser_IdAndAttendance_Id(userId, attendanceId);
        if (history.isEmpty())
            throw new TechnicianLocationNotFoundException(
                    "No location history found for user ID " + userId + " and attendance ID " + attendanceId);
        return mapper.toResponseList(history);
    }

    @Override
    public List<TechnicianLocationResponse> getAll() {
        List<TechnicianLocation> all = locationRepository.findAll();
        if (all.isEmpty())
            throw new TechnicianLocationNotFoundException("No location records found");
        return mapper.toResponseList(all);
    }
}
