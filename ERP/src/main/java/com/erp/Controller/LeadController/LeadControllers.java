package com.erp.Controller.LeadController;

import com.erp.Dto.Request.LeadRequest;
import com.erp.Model.Leads;
import com.erp.Service.lead.LeadServices;

import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Slf4j
public class LeadControllers {

    private final LeadServices leadService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ResponseStructure<Leads>> addOrUpdateLeads(@RequestBody LeadRequest request) {
        log.info("API /api/leads/addOrUpdate called");
        Leads savedLead = leadService.addOrUpdateLead(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Leave request submitted", savedLead);
    }

}
