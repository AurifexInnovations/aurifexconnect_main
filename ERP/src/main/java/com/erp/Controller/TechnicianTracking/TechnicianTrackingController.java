package com.erp.Controller.TechnicianTracking;

import com.erp.Dto.Request.TechTrackDto;
import com.erp.Service.TechnicianTracking.TechTrackService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class TechnicianTrackingController {

    private final TechTrackService techTrackService;

    @PostMapping("/v1/track/update")
    public ResponseEntity<ResponseStructure<TechTrackDto>> updateTechnicianLocation(@RequestBody TechTrackDto techTrackDto){
        return ResponseBuilder.success(HttpStatus.OK, "Technician Location Updated !!", techTrackService.updateLocation(techTrackDto));
    }

    @GetMapping("/v1/track")
    public ResponseEntity<ResponseStructure<TechTrackDto>> getByTechnicianId(@RequestParam Long technicianId){
        return ResponseBuilder.success(HttpStatus.OK, "Technician Location Fetched !!", techTrackService.findById(technicianId));
    }

}
