package com.erp.Controller.customer;

import com.erp.Dto.Request.CustomerDetailsRequestDto;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.*;
import com.erp.Model.CustomerDetails;
import com.erp.Service.Cutomer.CustomerDetailsService;

import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerDetailsController {

    private final CustomerDetailsService customerService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ResponseStructure<CustomerResponseDtos>> addOrUpdateCustomer(@RequestBody CustomerDetailsRequestDto request) {
        log.info("Received request to add/update customer: {}", request.getCustomerName());
        CustomerResponseDtos response = customerService.addOrUpdateCustomer(request);
        log.info("Customer processed successfully, ID: {}", response.getId());
        return ResponseBuilder.success(HttpStatus.CREATED, "CustomerDetailsService added successfully", response);
    }

    @PostMapping("/filter")
    public ResponseEntity<ResultDto<CustomerResponse>> filter(@RequestBody FilterRequest filterRequest) {
        return ResponseEntity.ok(customerService.getFilteredCustomers(filterRequest));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseStructure<CustomerResponse>> addCustomer(@RequestBody CustomerDetailsRequestDto customerDetailsRequestDto) {
        return ResponseBuilder.success(HttpStatus.OK, "Customer Added !!", customerService.addCustomer(customerDetailsRequestDto));
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<ResultDto<CustomerResponse>>> getAllCustomers(){
        return ResponseBuilder.success(HttpStatus.OK, "All Customers Fetched !!", customerService.getAll());
    }

    @GetMapping("/byId")
    public ResponseEntity<ResponseStructure<CustomerResponse>> getCustomerById(@RequestParam long id){
        return ResponseBuilder.success(HttpStatus.OK, "Customer Fetched !!", customerService.getById(id));
    }

    @DeleteMapping
    public ResponseEntity<ResponseStructure<CustomerResponse>> deleteCustomerById(@RequestParam long id){
        return ResponseBuilder.success(HttpStatus.OK, "Delete Customer Successfully !!", customerService.deleteById(id));
    }

    @GetMapping("/branchWise")
    public ResponseEntity<ResponseStructure<ResultDto<CustomerResponse>>> getAllBranchWise(){
        return ResponseBuilder.success(HttpStatus.OK, "Customers Fetched Branch Wise !!", customerService.getByBranchWise());
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ResponseStructure<ResultDto<DropDown>>> getAllCustomersDropDown(){
        return ResponseBuilder.success(HttpStatus.OK, "Customers Drop Down Details Branch Wise", customerService.getDropdown());
    }

    @PostMapping("/byId/track")
    public ResponseEntity<ResponseStructure<ResultDto<TechnicianResponseDTO>>> getAllCustomerWithTaskDetails(@RequestBody FilterRequest filterRequest){
        return ResponseBuilder.success(HttpStatus.OK, "Customers With Task Details !!", customerService.findCustomerWithTask(filterRequest));
    }

}
