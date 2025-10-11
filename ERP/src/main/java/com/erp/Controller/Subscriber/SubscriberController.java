package com.erp.Controller.Subscriber;


import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Service.SubscriptionService.ISubscriptionPlanService;
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
@RequestMapping("/api/v1/subscriber")
@Tag(name = "Subscriber Controller", description = "APIs for User Subscription")
public class SubscriberController {

    @Autowired
    private ISubscriptionPlanService subscriptionPlanService;

    @PostMapping("/addUserSubscription")
    @Operation(description = "API to Create a User Subscription",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User Subscription Request Created")
            })
    public ResponseEntity<ResponseStructure<UserSubscriptionResponse>> addUserSubscription(@Valid @RequestBody UserSubscriptionRequest request) {
        UserSubscriptionResponse response = subscriptionPlanService.createUserSubscription(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "User Subscription Created", response);
    }

    @GetMapping("/userId/{userId}")
    @Operation(description = "API to fetch a User-Subscription by user id",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User Subscription Request Created")
            })
    public ResponseEntity<ResponseStructure<UserSubscriptionResponse>> fetchUserSubscription(@PathVariable String userId) {
        UserSubscriptionResponse response = subscriptionPlanService.fetchUserSubscription(userId);
        return ResponseBuilder.success(HttpStatus.CREATED, "User Subscription Created", response);
    }

    @GetMapping("/user/{userId}")
    @Operation(description = "API to fetch a plan validity by user id",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User Subscription Request Created")
            })
    public ResponseEntity<ResponseStructure<String>> checkPlanValidity(@PathVariable String userId) {
        String response = subscriptionPlanService.checkPlanValidity(userId);
        return ResponseBuilder.success(HttpStatus.CREATED, "User Subscription Created", response);
    }
}
