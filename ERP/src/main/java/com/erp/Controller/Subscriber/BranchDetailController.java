package com.erp.Controller.Subscriber;

import com.erp.Dto.Request.BranchRqst;
import com.erp.Dto.Response.BranchRpns;
import com.erp.Dto.SubscriptionsDto.BranchDto;
import com.erp.Service.SubscriptionService.IBranchDetailService;
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
@RequestMapping("/api/v1/branch")
@Tag(name = "Branch Controller", description = "APIs for Branch Details")
public class BranchDetailController {

    @Autowired
    private IBranchDetailService branchService;

    @GetMapping("/code/{branchCode}")
    @Operation(description = "Fetch branch details by branch code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branch details retrieved")
            })
    public ResponseEntity<ResponseStructure<BranchDto>> fetchBranchDetails(@PathVariable String branchCode) {
        BranchDto response = branchService.fetchBranchByCode(branchCode);
        return ResponseBuilder.success(HttpStatus.OK, "Retrieved Branch Details.", response);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<BranchRpns>> createBranch(
            @RequestBody BranchRqst request) {
        BranchRpns response = branchService.createBranch(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Branch created successfully", response);
    }

}