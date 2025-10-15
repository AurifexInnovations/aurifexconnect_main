package com.erp.Controller.Subscriber;

import com.erp.Dto.Request.TechnicianRqst;
import com.erp.Dto.Response.TechnicianResponse;
import com.erp.Dto.SubscriptionsDto.TechnicianDto;
import com.erp.Service.SubscriptionService.ITechnicianService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/technician")
@Tag(name = "Technician Controller", description = "APIs for Technician Details")
public class TechnicianController {

    @Autowired
    private ITechnicianService technicianService;

    @GetMapping("/id/{technicianId}")
    @Operation(description = "Fetch technician details by technician ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Technician details retrieved")
            })
    public ResponseEntity<ResponseStructure<TechnicianDto>> fetchTechnicianDetails(@PathVariable Long technicianId) {
        TechnicianDto response = technicianService.fetchTechnicianById(technicianId);
        return ResponseBuilder.success(HttpStatus.OK, "Retrieved Technician Details.", response);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<TechnicianResponse>> createTechnician(
            @RequestBody TechnicianRqst request) {
        TechnicianResponse response = technicianService.createTechnician(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Technician created successfully", response);
    }

}