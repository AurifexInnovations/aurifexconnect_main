package com.erp.Controller.comapnyDetails;


import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
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
public class CompanyDetailsController
{
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
    public ResponseEntity<CompanyDetailsResponseDto> getById(@RequestParam final String id)
    {
        return service.findById(Long.valueOf(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

}
