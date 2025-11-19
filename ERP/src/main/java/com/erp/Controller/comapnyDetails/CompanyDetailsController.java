package com.erp.Controller.comapnyDetails;


import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.CompanyDetails.CompanyDetailsService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/company")
@Tag(name = "Company Creation", description = "API Endpoints for Company Creation")
public class CompanyDetailsController {
    private final CompanyDetailsService service;

    @GetMapping
    @Operation(
            summary = "Get By Company ID",
            description = "Retrieve company by company id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Company details retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    public ResponseEntity<ResponseStructure<CompanyDetailsResponseDto>> getById(@RequestParam final Long id) {
//        return service.findById(Long.valueOf(id))
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
        return ResponseBuilder.success(HttpStatus.OK, "Company Details Fetched Successfully", service.findBySingleId(id));
    }

    @PostMapping
    @Operation(description = "Create a company or update if already exists - onboarding of client",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Company Created Successfully or Updated Successfully"),
            })
    public ResponseEntity<ResponseStructure<CompanyDetailsResponseDto>> create(
            @RequestBody final CompanyDetailsRequestDto companyDetails) {
        return ResponseBuilder.success(HttpStatus.OK,
                "Company created successfully!!",
                service.saveAndUpdate(companyDetails));
    }

    @PutMapping("/review")
    public ResponseEntity<ResponseStructure<CompanyDetailsResponseDto>> reviewCompnayDeatils(@RequestBody CompanyDetailsRequestDto companyDetailsRequestDto){
        return ResponseBuilder.success(HttpStatus.OK,
                "Company Reviewed successfully!!",
                service.reviewCompany(companyDetailsRequestDto));
    }

    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<CompanyDetailsResponseDto>>> filterCompanyDetails(
            @RequestBody FilterRequest filterRequest) {
        return ResponseBuilder.success(HttpStatus.OK,
                "All Company Details",
                service.getFilterData(filterRequest));
    }

    @GetMapping("/email")
    public ResponseEntity<ResponseStructure<CompanyDetailsResponseDto>> getByCompanyEmail()
    {
        return ResponseBuilder.success(HttpStatus.OK, "Company Details Retrieved", service.getByEmail());
    }
}
