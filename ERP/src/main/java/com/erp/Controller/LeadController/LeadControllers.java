package com.erp.Controller.LeadController;

import com.erp.Dto.Request.LeadProductRequestDto;
import com.erp.Dto.Request.LeadRequest;
import com.erp.Dto.Request.LeadResponse;
import com.erp.Dto.Response.ResultDto;
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
        return ResponseBuilder.success(HttpStatus.CREATED, "Lead request submitted", savedLead);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseStructure<LeadResponse>> addLead(@RequestBody LeadRequest request)
    {
        LeadResponse leadResponse = leadService.addService(request);
        return ResponseBuilder.success(HttpStatus.OK, "Lead Created!!", leadResponse);
    }


    @GetMapping()
    public ResponseEntity<ResponseStructure<ResultDto<LeadResponse>>> getAllLeads()
    {
        ResultDto<LeadResponse> resultDto = leadService.getAllLeads();
        return ResponseBuilder.success(HttpStatus.OK, "Leads Fetched Successfully !!", resultDto);
    }

    @GetMapping("/byId")
    public ResponseEntity<ResponseStructure<LeadResponse>> getById(@RequestParam long id)
    {
        return ResponseBuilder.success(HttpStatus.OK, "Lead Fetched Successfully !!", leadService.getById(id));
    }

    @DeleteMapping
    public ResponseEntity<ResponseStructure<LeadResponse>> deleteById(@RequestParam long id)
    {
        return ResponseBuilder.success(HttpStatus.OK, "Lead Deleted Successfully !!", leadService.deleteById(id));
    }

    @PutMapping("/review")
    public ResponseEntity<ResponseStructure<LeadResponse>> updateStatus(@RequestBody LeadRequest leadRequest)
    {
        return ResponseBuilder.success(HttpStatus.OK, "Lead Update Status Successfully !!", leadService.updateStatus(leadRequest));
    }
}
