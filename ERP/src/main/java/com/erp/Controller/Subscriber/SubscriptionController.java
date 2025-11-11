package com.erp.Controller.Subscriber;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import com.erp.Service.SubscriptionService.ISubscriptionService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
@Tag(name = "Subscription Controller", description = "APIs for Subscription Details")
public class SubscriptionController {

    @Autowired
    private ISubscriptionService subscriptionService;

    @GetMapping("/id/{userId}")
    @Operation(description = "Fetch subscription details by subscription ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subscription details retrieved")
            })
    public ResponseEntity<ResponseStructure<SubscriptionDto>> fetchSubscriptionDetails(@PathVariable String userId) {
        SubscriptionDto response = subscriptionService.fetchSubscriptionByUserId(userId);
        return ResponseBuilder.success(HttpStatus.OK, "Retrieved Subscription Details.", response);
    }

    @PostMapping("/addUserSubscription")
    @Operation(description = "API to Create a User Subscription",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User Subscription Request Created")
            })
    public ResponseEntity<ResponseStructure<UserSubscriptionResponse>> addUserSubscription(
            @Valid @RequestBody UserSubscriptionRequest request) {

        UserSubscriptionResponse response = subscriptionService.createUserSubscription(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "User Subscription Created", response);
    }


    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<SubscriptionDto>>> subscriptionsFilter(
            @RequestBody FilterRequest filterRequest){

        ResultDto<SubscriptionDto> resultDto = subscriptionService.fetchFIlterSubscription(filterRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Subscriptins Fetched By Filter", resultDto);
    }
}