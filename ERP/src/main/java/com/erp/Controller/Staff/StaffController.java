package com.erp.Controller.Staff;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Request.StaffParam;
import com.erp.Dto.Request.StaffRequest;
import com.erp.Dto.Response.StaffResponse;
import com.erp.Service.StaffService.StaffService;
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
@Tag(name = "Staff Controller", description = "Collection of API Endpoints Dealing with Staff Data")
public class StaffController {

    private final StaffService staffService;

    @PostMapping("staff")
    @Operation(description = "API Endpoint to Create a New Staff",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Staff Created Successfully"),
            })
    public ResponseEntity<ResponseStructure<StaffResponse>> createStaff(@Valid @RequestBody StaffRequest staffRequest){
        StaffResponse staffResponse = staffService.createStaff(staffRequest);
        return ResponseBuilder.success(HttpStatus.CREATED, "Staff Created", staffResponse);
    }

    @PutMapping("staff/update")
    @Operation(description = "API Endpoint to Update Existing Staff",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Staff Updated Successfully"),
                    @ApiResponse(responseCode = "404", description = "Staff Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<StaffResponse>> updateStaff(@RequestBody StaffRequest staffRequest){
        StaffResponse staffResponse = staffService.updateStaff(staffRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Staff Updated Successfully!", staffResponse);
    }

    @DeleteMapping("staff/delete")
    @Operation(description = "API Endpoint to Delete a Staff By ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Staff Deleted Successfully"),
                    @ApiResponse(responseCode = "404", description = "Staff Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<StaffResponse>> deleteStaffById(@RequestBody CommanParam param){
        StaffResponse staffResponse = staffService.deleteStaffById(param);
        return ResponseBuilder.success(HttpStatus.OK, "Staff Deleted Successfully!", staffResponse);
    }

    @PostMapping("staff/by-details")
    @Operation(description = "API Endpoint to Retrieve Staff by ID, Name, Designation or Status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Staff Found Successfully"),
                    @ApiResponse(responseCode = "404", description = "Staff Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<StaffResponse>> getStaffByIdOrNameOrDesignationOrStatus(@RequestBody StaffParam param){
        List<StaffResponse> staffResponses = staffService.getStaffByIdOrNameOrDesignationOrStatus(param);
        return ResponseBuilder.success(HttpStatus.OK, "Staff Found Successfully", staffResponses);
    }

    @PostMapping("staff/all")
    @Operation(description = "API Endpoint to Retrieve All Staff with Pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Staff Found Successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid Pagination Parameters", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<StaffResponse>> getAllStaff(@RequestBody PaginationRequest request){
        List<StaffResponse> staffResponses = staffService.getAllStaff(request);
        return ResponseBuilder.success(HttpStatus.OK, "Staff fetched successfully!", staffResponses);
    }

    @PostMapping("staff/by-branch")
    @Operation(description = "API Endpoint to Retrieve Staff by Branch ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Staff Retrieved Successfully"),
                    @ApiResponse(responseCode = "404", description = "No Staff Found for Given Branch", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<StaffResponse>> getStaffByBranchId(@RequestBody CommanParam param){
        List<StaffResponse> staffResponses = staffService.getStaffByBranchId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Staff retrieved successfully!", staffResponses);
    }
}
