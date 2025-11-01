package com.erp.Controller.crm;

import com.erp.Dto.Request.LeadRequestDto;
import com.erp.Dto.Response.LeadResponseDto;
import com.erp.Service.crm.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lead")
@AllArgsConstructor
@Tag(name = "Leads API", description = "Operations on Leads")
public class LeadController {
    private final LeadService service;

    @Operation(summary = "Create contact")
    @PostMapping("/create")
    public ResponseEntity<LeadResponseDto> create(
            @RequestBody LeadRequestDto dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Fetch contact by ID")
    @GetMapping("/record/{id}")
    public ResponseEntity<LeadResponseDto> fetchById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

}
