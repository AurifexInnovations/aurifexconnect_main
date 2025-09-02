package com.erp.TechnicianApp.TechnicianService.TechnicianShiftService;

import com.erp.Model.User;
import com.erp.Repository.User.UserRepository;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianShiftRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianShiftResponse;
import com.erp.TechnicianApp.TechnicianException.Shift.NoActiveShiftException;
import com.erp.TechnicianApp.TechnicianException.Shift.ShiftAlreadyActiveException;
import com.erp.TechnicianApp.TechnicianException.Shift.TechnicianNotFoundException;
import com.erp.TechnicianApp.TechnicianMapper.TechnicianShift.TechnicianShiftMapper;
import com.erp.TechnicianApp.TechnicianModel.TechnicianShift;
import com.erp.TechnicianApp.TechnicianRepository.TechnicianShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianShiftServiceImpl implements TechnicianShiftService {

    private final TechnicianShiftRepository shiftRepository;
    private final TechnicianShiftMapper mapper;
    private final UserRepository userRepository;

    @Override
    public TechnicianShiftResponse startShift(Long userId) {
        if (shiftRepository.findByUser_IdAndActiveTrue(userId).isPresent()) {
            throw new ShiftAlreadyActiveException("Technician " + userId + " already has an active shift.");
        }

        User technician = userRepository.findById(userId)
                .orElseThrow(() -> new TechnicianNotFoundException("Technician not found with id " + userId));

        TechnicianShift shift = new TechnicianShift();
        shift.setUser(technician);
        shift.setShiftName("Auto Shift");
        shift.setShiftStart(LocalDateTime.now());
        shift.setShiftType("REGULAR");
        shift.setActive(true);

        return mapper.toResponse(shiftRepository.save(shift));
    }

    @Override
    public TechnicianShiftResponse endShift(Long userId) {
        TechnicianShift shift = shiftRepository.findByUser_IdAndActiveTrue(userId)
                .orElseThrow(() -> new NoActiveShiftException("No active shift found for technician " + userId));

        shift.setShiftEnd(LocalDateTime.now());
        shift.setActive(false);

        return mapper.toResponse(shiftRepository.save(shift));
    }

    @Override
    public TechnicianShiftResponse getActiveShift(Long userId) {
        TechnicianShift shift = shiftRepository.findByUser_IdAndActiveTrue(userId)
                .orElseThrow(() -> new NoActiveShiftException("No active shift found for technician " + userId));
        return mapper.toResponse(shift);
    }

    @Override
    public TechnicianShiftResponse createShift(TechnicianShiftRequest request) {
        if (shiftRepository.findByUser_IdAndActiveTrue(request.getUserId()).isPresent()) {
            throw new ShiftAlreadyActiveException("Technician " + request.getUserId() + " already has an active shift.");
        }

        User technician = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new TechnicianNotFoundException("Technician not found with id " + request.getUserId()));

        TechnicianShift shift = mapper.toEntity(request);
        shift.setUser(technician);

        if (shift.getShiftStart() == null) {
            shift.setShiftStart(LocalDateTime.now());
        }

        shift.setActive(true);
        return mapper.toResponse(shiftRepository.save(shift));
    }

    @Override
    public List<TechnicianShiftResponse> getShiftsByTechnician(Long userId) {
        List<TechnicianShift> shifts = shiftRepository.findByUser_IdOrderByShiftStartDesc(userId);
        if (shifts.isEmpty()) {
            throw new TechnicianNotFoundException("No shifts found for technician with id: " + userId);
        }
        return mapper.toResponseList(shifts);
    }

    @Override
    public List<TechnicianShiftResponse> getAllShifts() {
        return mapper.toResponseList(shiftRepository.findAll());
    }

    @Override
    public void deleteShift(Long shiftId) {
        shiftRepository.deleteById(shiftId);
    }

    @Override
    public void deleteShiftsByUser(Long userId) {
        List<TechnicianShift> shifts = shiftRepository.findByUser_IdOrderByShiftStartDesc(userId);
        if (shifts.isEmpty()) {
            throw new TechnicianNotFoundException("No shifts found for technician with id: " + userId);
        }
        shiftRepository.deleteAll(shifts);
    }
}
