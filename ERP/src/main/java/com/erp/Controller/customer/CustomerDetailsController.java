package com.erp.Controller.customer;

import com.erp.Dto.Request.CustomerDetailsRequestDto;

import com.erp.Dto.Response.CustomerResponseDtos;
import com.erp.Service.Cutomer.CustomerDetailsService;

import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerDetailsController {

    @Autowired
    private CustomerDetailsService customerService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ResponseStructure<CustomerResponseDtos>> addOrUpdateCustomer(@RequestBody CustomerDetailsRequestDto request) {
        log.info("Received request to add/update customer: {}", request.getCustomerName());
        CustomerResponseDtos response = customerService.addOrUpdateCustomer(request);
        log.info("Customer processed successfully, ID: {}", response.getId());
        return ResponseBuilder.success(HttpStatus.CREATED, "CustomerDetailsService added successfully", response);
    }
}
