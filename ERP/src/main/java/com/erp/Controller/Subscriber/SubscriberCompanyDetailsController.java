package com.erp.Controller.Subscriber;

import com.erp.Dto.Request.CompanyDetailsRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.SubscriptionsDto.CompanyDetailsDto;
import com.erp.Service.SubscriptionService.ICompanyDetailsService;
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
@RequestMapping("/api/v1/company")
@Tag(name = "CompanyDetails Controller", description = "APIs for Company Details")
public class SubscriberCompanyDetailsController {

    @Autowired
    private ICompanyDetailsService companyDetailsService;

    @GetMapping("/code/{companyCode}")
    @Operation(description = "Fetch company details by company code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Company details retrieved")
            })
    public ResponseEntity<ResponseStructure<CompanyDetailsDto>> fetchCompanyDetails(@PathVariable String companyCode) {
        CompanyDetailsDto response = companyDetailsService.fetchCompanyDetailsByCode(companyCode);
        return ResponseBuilder.success(HttpStatus.OK, "Retrieved Company Details.", response);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<CompanyDetailsResponse>> createCompany(
            @RequestBody CompanyDetailsRequest request) {
        CompanyDetailsResponse response = companyDetailsService.createCompany(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Company created successfully", response);
    }

}