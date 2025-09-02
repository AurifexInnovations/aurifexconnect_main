package com.erp.TechnicianApp.TechnicianService.TechnicianShiftService;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianShiftRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianShiftResponse;

import java.util.List;

public interface TechnicianShiftService {

    TechnicianShiftResponse startShift(Long userId);

    TechnicianShiftResponse endShift(Long userId);

    TechnicianShiftResponse getActiveShift(Long userId);

    TechnicianShiftResponse createShift(TechnicianShiftRequest request);

    List<TechnicianShiftResponse> getShiftsByTechnician(Long userId);

    List<TechnicianShiftResponse> getAllShifts();

    void deleteShift(Long shiftId);

    void deleteShiftsByUser(Long userId);
}