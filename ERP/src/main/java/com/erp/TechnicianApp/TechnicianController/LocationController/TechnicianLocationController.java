package com.erp.TechnicianApp.TechnicianController.LocationController;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationImageRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianLocationResponse;
import com.erp.TechnicianApp.TechnicianService.TechnicianLocation.TechnicianLocationService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/technician/location")
@RequiredArgsConstructor
public class TechnicianLocationController {

    private final TechnicianLocationService locationService;

    /** Save technician location (without image) */
    @PostMapping("/push")
    public ResponseEntity<ResponseStructure<TechnicianLocationResponse>> pushLocation(
            @RequestBody TechnicianLocationRequest request) {
        TechnicianLocationResponse response = locationService.pushLocation(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Technician location saved successfully", response);
    }

    /** Save technician location with image proof */
    @PostMapping("/push-with-image")
    public ResponseEntity<ResponseStructure<TechnicianLocationResponse>> pushLocationWithImage(
            @ModelAttribute TechnicianLocationImageRequest request) {
        TechnicianLocationResponse response = locationService.pushLocationWithImage(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Technician location with image saved successfully", response);
    }

    /** Get latest location of a technician */
    @PostMapping("/latest")
    public ResponseEntity<ResponseStructure<TechnicianLocationResponse>> getLatest(
            @RequestBody TechnicianLocationRequest request) {
        TechnicianLocationResponse response = locationService.getLatest(request.getUserId());
        return ResponseBuilder.success(HttpStatus.OK, "Latest technician location fetched successfully", response);
    }

    /** Get latest location for a technician's attendance */
    @PostMapping("/latest/attendance")
    public ResponseEntity<ResponseStructure<TechnicianLocationResponse>> getLatestForAttendance(
            @RequestBody TechnicianLocationRequest request) {
        TechnicianLocationResponse response = locationService.getLatestForAttendance(
                request.getUserId(), request.getAttendanceId());
        return ResponseBuilder.success(HttpStatus.OK, "Latest location for attendance fetched successfully", response);
    }

    /** Get history between start & end date */
    @PostMapping("/history")
    public ResponseEntity<ListResponseStructure<TechnicianLocationResponse>> getHistory(
            @RequestBody TechnicianLocationRequest request) {
        List<TechnicianLocationResponse> responses = locationService.getHistory(
                request.getUserId(), request.getRecordedAt(), request.getRecordedAt());
        return ResponseBuilder.success(HttpStatus.OK, "Technician location history fetched successfully", responses);
    }

    /** Get history for a specific attendance */
    @PostMapping("/history/attendance")
    public ResponseEntity<ListResponseStructure<TechnicianLocationResponse>> getHistoryForAttendance(
            @RequestBody TechnicianLocationRequest request) {
        List<TechnicianLocationResponse> responses = locationService.getHistoryForAttendance(
                request.getUserId(), request.getAttendanceId());
        return ResponseBuilder.success(HttpStatus.OK, "Technician location history for attendance fetched successfully", responses);
    }

    /** Get all stored locations */
    @PostMapping("/all")
    public ResponseEntity<ListResponseStructure<TechnicianLocationResponse>> getAll() {
        List<TechnicianLocationResponse> responses = locationService.getAll();
        return ResponseBuilder.success(HttpStatus.OK, "All technician locations fetched successfully", responses);
    }
}
