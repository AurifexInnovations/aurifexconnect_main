package com.erp.Controller.Branch;

import com.erp.Dto.Request.BranchRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Service.BranchService.BranchService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/")
@Tag(name = "Branch Controller", description = "Collection of API Endpoints Dealing with Branch Data")
public class BranchController {

    private final BranchService branchService;

    @PostMapping("branch")
    @Operation(description = "API Endpoint to Create a New Branch",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Branch Created Successfully"),
            })
    public ResponseEntity<ResponseStructure<BranchResponse>> createBranch(@Valid @RequestBody BranchRequest branchRequest){
        BranchResponse branchResponse = branchService.createBranch(branchRequest);
        return ResponseBuilder.success(HttpStatus.CREATED,"Branch Created", branchResponse);
    }

    @PutMapping("branch/update")
    @Operation(description = "API Endpoint to Update Existing Branch",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branch Updated Successfully"),
                    @ApiResponse(responseCode = "404", description = "Branch Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<BranchResponse>> updateBranch(@RequestBody BranchRequest branchRequest){
        BranchResponse branchResponse = branchService.updateBranch(branchRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Branch Updated Successfully!", branchResponse);
    }

    @DeleteMapping("branch/delete")
    @Operation(description = "API Endpoint to Delete a Branch By ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branch Deleted Successfully"),
                    @ApiResponse(responseCode = "404", description = "Branch Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<BranchResponse>> deleteBranchById(@RequestBody CommanParam param){
        BranchResponse branchResponse = branchService.deleteBranchById(param);
        return ResponseBuilder.success(HttpStatus.OK,"Branch Deleted Successfully!",branchResponse);
    }

    @PostMapping("branch/byid")
    @Operation(description = "API Endpoint to Retrieve Branch by ID or Name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branch Found Successfully"),
                    @ApiResponse(responseCode = "404", description = "Branch Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<BranchResponse>> getByIdOrBranchNameOrBranchLocationOrBranchStatus(@RequestBody CommanParam param){
        List<BranchResponse> branchResponse = branchService.getByIdOrBranchNameOrLocationOrBranchStatus(param);
        return ResponseBuilder.success(HttpStatus.OK,"Branch Found Successfully",branchResponse);
    }

    @PostMapping("branch/all")
    @Operation(description = "API Endpoint to Retrieve All Branches with Pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Branches Found Successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid Pagination Parameters", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<BranchResponse>> getAllBranches(
            @RequestBody PaginationRequest request) {

        List<BranchResponse> branchResponse = branchService.getAllBranches(request);
        return ResponseBuilder.success(HttpStatus.OK, "Branches fetched successfully!", branchResponse);
    }

    @PostMapping("/branch/by-item")
    @Operation(description = "API Endpoint to Retrieve Branches by Item Name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Branches Retrieved Successfully"),
                    @ApiResponse(responseCode = "404", description = "No Branches Found for Given Item", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<BranchResponse>> getBranchesByItemName(@RequestBody CommanParam param){
        List<BranchResponse> branchResponse = branchService.getBranchesByItemName(param);
        return ResponseBuilder.success(HttpStatus.OK,"Branches retrieved successfully!",branchResponse);
    }
}

