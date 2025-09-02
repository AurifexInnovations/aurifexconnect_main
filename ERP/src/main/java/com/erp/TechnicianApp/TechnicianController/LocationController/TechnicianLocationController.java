package com.erp.TechnicianApp.TechnicianController.LocationController;

import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationRequest;
import com.erp.TechnicianApp.TechnicianDto.Request.TechnicianLocationImageRequest;
import com.erp.TechnicianApp.TechnicianDto.Response.TechnicianLocationResponse;
import com.erp.TechnicianApp.TechnicianService.TechnicianLocation.TechnicianLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/technician/location")
@RequiredArgsConstructor
public class TechnicianLocationController {

    private final TechnicianLocationService locationService;

    /**
     * Save technician location (without image)
     */
    @PostMapping("/push")
    public ResponseEntity<TechnicianLocationResponse> pushLocation(
            @RequestBody TechnicianLocationRequest request) {
        return ResponseEntity.ok(locationService.pushLocation(request));
    }

    /**
     * Save technician location with image proof
     */
    @PostMapping("/push-with-image")
    public ResponseEntity<TechnicianLocationResponse> pushLocationWithImage(
            @ModelAttribute TechnicianLocationImageRequest request) {
        return ResponseEntity.ok(locationService.pushLocationWithImage(request));
    }

    /**
     * Get latest location of a technician (general)
     */
    @GetMapping("/latest/{userId}")
    public ResponseEntity<TechnicianLocationResponse> getLatest(@PathVariable Long userId) {
        return ResponseEntity.ok(locationService.getLatest(userId));
    }

    /**
     * Get latest location for a technician's attendance
     */
    @GetMapping("/latest/{userId}/attendance/{attendanceId}")
    public ResponseEntity<TechnicianLocationResponse> getLatestForAttendance(
            @PathVariable Long userId,
            @PathVariable Long attendanceId) {
        return ResponseEntity.ok(locationService.getLatestForAttendance(userId, attendanceId));
    }

    /**
     * Get history between start & end date
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<TechnicianLocationResponse>> getHistory(
            @PathVariable Long userId,
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(locationService.getHistory(userId, start, end));
    }

    /**
     * Get history for a specific attendance
     */
    @GetMapping("/history/{userId}/attendance/{attendanceId}")
    public ResponseEntity<List<TechnicianLocationResponse>> getHistoryForAttendance(
            @PathVariable Long userId,
            @PathVariable Long attendanceId) {

        return ResponseEntity.ok(locationService.getHistoryForAttendance(userId, attendanceId));
    }

    /**
     * Get all stored locations
     */
    @GetMapping("/all")
    public ResponseEntity<List<TechnicianLocationResponse>> getAll() {
        return ResponseEntity.ok(locationService.getAll());
    }
}
