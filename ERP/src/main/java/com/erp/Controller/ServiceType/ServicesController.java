package com.erp.Controller.ServiceType;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.ServiceRequest;
import com.erp.Dto.Response.ServiceResponse;
import com.erp.Service.ServiceType.ServiceType;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/service")
@Tag(name = "Service Controller", description = "Collection of API Endpoints for Managing Service Types")
public class ServicesController {

    private final ServiceType serviceTypeService;

    // POST /service
    @PostMapping
    @Operation(
            summary = "Add a new service",
            description = "API to add a new service record.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Service added successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            }
    )
    public ResponseEntity<ResponseStructure<ServiceResponse>> addService(@RequestBody ServiceRequest serviceRequest) {
        ServiceResponse servicesResponse = serviceTypeService.addService(serviceRequest);
        return ResponseBuilder.success(HttpStatus.CREATED, "Service added successfully!!", servicesResponse);
    }

    // PUT /service/update
    @PutMapping("/update")
    @Operation(
            summary = "Update an existing service",
            description = "API to update a service based on its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Service updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Service not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            }
    )
    public ResponseEntity<ResponseStructure<ServiceResponse>> updateServices(@RequestBody ServiceRequest serviceRequest) {
        ServiceResponse servicesResponse = serviceTypeService.updateById(serviceRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Service updated successfully!!", servicesResponse);
    }

    // POST /service/byid
    @PostMapping("/byid")
    @Operation(
            summary = "Find services by ID or name",
            description = "API to retrieve services by service ID or name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Services retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Services not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            }
    )
    public ResponseEntity<ListResponseStructure<ServiceResponse>> findByIdOrServiceName(@RequestBody CommanParam param) {
        List<ServiceResponse> servicesResponse = serviceTypeService.findByIdOrServiceName(param);
        return ResponseBuilder.success(HttpStatus.OK, "Services retrieved successfully!!", servicesResponse);
    }

    // DELETE /service/delete
    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete a service by ID",
            description = "API to delete a service using its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Service deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Service not found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            }
    )
    public ResponseEntity<ResponseStructure<ServiceResponse>> deleteByServiceId(@RequestBody CommanParam param) {
        ServiceResponse response = serviceTypeService.deleteByServiceId(param);
        return ResponseBuilder.success(HttpStatus.OK, "Service deleted successfully!!", response);
    }

    // GET /service/all
    @GetMapping("/all")
    @Operation(
            summary = "Fetch all services",
            description = "API to retrieve all service records.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Services fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "No services found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            }
    )
    public ResponseEntity<ListResponseStructure<ServiceResponse>> fetchAllServices() {
        List<ServiceResponse> servicesResponse = serviceTypeService.fetchAllServices();
        return ResponseBuilder.success(HttpStatus.OK, "All services fetched successfully!!", servicesResponse);
    }

    @GetMapping("/status")
    @Operation(
            summary = "Find services by status",
            description = "This API fetches services filtered by their status (ACTIVE, INACTIVE, DELETED).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Services fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "No services found")
            }
    )
    public ResponseEntity<ListResponseStructure<ServiceResponse>> findByStatus(@RequestBody ServiceRequest serviceRequest) {
        List<ServiceResponse> servicesResponse = serviceTypeService.findByStatus(serviceRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Services fetched successfully by status!!", servicesResponse);
    }

    // GET /service/categories
    @GetMapping("/categories")
    @Operation(
            summary = "Fetch all distinct service categories",
            description = "API to retrieve all unique service categories available in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories fetched successfully"),
                    @ApiResponse(responseCode = "404", description = "No categories found",
                            content = @Content(schema = @Schema(implementation = SimpleErrorResponse.class)))
            }
    )
    public ResponseEntity<ListResponseStructure<String>> fetchAllCategories() {
        List<String> categories = serviceTypeService.fetchAllCategories();
        return ResponseBuilder.success(HttpStatus.OK, "Categories fetched successfully!", categories);
    }

    @PostMapping("/bycategory")
    public ResponseEntity<ListResponseStructure<ServiceResponse>> fetchServiceByCategory(@RequestBody ServiceRequest serviceRequest){
        List<ServiceResponse> serviceResponses = serviceTypeService.fetchServiceByCategory(serviceRequest);
        return ResponseBuilder.success(HttpStatus.OK,"Service Found By Given Categories", serviceResponses);
    }
}
