package com.erp.TechnicianApp.TechnicianController.TechnicianShift;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianShiftRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianShiftResponse;
import com.erp.TechnicianApp.TechnicianService.TechnicianShiftService.TechnicianShiftService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/technician/shifts")
public class TechnicianShiftController {

    private final TechnicianShiftService shiftService;

    @PostMapping("/start")
    public ResponseEntity<ResponseStructure<TechnicianShiftResponse>> startShift(
            @Valid @RequestBody TechnicianShiftRequest request) throws Exception {
        TechnicianShiftResponse response = shiftService.startShift(request.getUserId());
        return ResponseBuilder.success(HttpStatus.OK, "Technician shift started successfully", response);
    }

    @PostMapping("/end")
    public ResponseEntity<ResponseStructure<TechnicianShiftResponse>> endShift(
            @Valid @RequestBody TechnicianShiftRequest request) throws Exception {
        TechnicianShiftResponse response = shiftService.endShift(request.getUserId());
        return ResponseBuilder.success(HttpStatus.OK, "Technician shift ended successfully", response);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<TechnicianShiftResponse>> createShift(
            @Valid @RequestBody TechnicianShiftRequest request) throws Exception {
        TechnicianShiftResponse response = shiftService.createShift(request);
        return ResponseBuilder.success(HttpStatus.OK, "Technician shift created successfully", response);
    }

    @PostMapping("/active")
    public ResponseEntity<ResponseStructure<TechnicianShiftResponse>> getActiveShift(
            @Valid @RequestBody TechnicianShiftRequest request) throws Exception {
        TechnicianShiftResponse response = shiftService.getActiveShift(request.getUserId());
        return ResponseBuilder.success(HttpStatus.OK, "Active technician shift fetched", response);
    }

    @PostMapping("/records")
    public ResponseEntity<ListResponseStructure<TechnicianShiftResponse>> getShiftsByTechnician(
            @Valid @RequestBody TechnicianShiftRequest request) throws Exception {
        List<TechnicianShiftResponse> responses = shiftService.getShiftsByTechnician(request.getUserId());
        return ResponseBuilder.success(HttpStatus.OK, "Technician shift records fetched", responses);
    }

    @PostMapping("/all")
    public ResponseEntity<ListResponseStructure<TechnicianShiftResponse>> getAllShifts() {
        List<TechnicianShiftResponse> responses = shiftService.getAllShifts();
        return ResponseBuilder.success(HttpStatus.OK, "All technician shift records fetched", responses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseStructure<String>> deleteShift(
            @Valid @RequestBody TechnicianShiftRequest request) {
        shiftService.deleteShift(request.getUserId());
        return ResponseBuilder.success(HttpStatus.OK, "Technician shift deleted successfully", "Deleted successfully");
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ResponseStructure<String>> deleteShiftsByUser(
            @Valid @RequestBody TechnicianShiftRequest request) throws Exception {
        shiftService.deleteShiftsByUser(request.getUserId());
        return ResponseBuilder.success(HttpStatus.OK, "All technician shifts deleted successfully", "Deleted successfully");
    }
}
