package com.erp.Controller.Subscriber;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SubscriptionAdminResponse;
import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import com.erp.Service.SubscriptionService.ISubscriptionService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.engine.spi.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
@Tag(name = "Subscription Controller", description = "APIs for Subscription Details")
public class SubscriptionController {

    @Autowired
    private ISubscriptionService subscriptionService;

    @PreAuthorize("hasAnyAuthority('ROLE_ROOT', 'ROLE_ADMIN')")
    @GetMapping("/id/{userId}")
    @Operation(description = "Fetch subscription details by subscription ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subscription details retrieved")
            })
    public ResponseEntity<ResponseStructure<SubscriptionAdminResponse>> fetchSubscriptionDetails(@PathVariable String userId) {
        SubscriptionAdminResponse response = subscriptionService.fetchSubscriptionAdminByUserId(userId);
        return ResponseBuilder.success(HttpStatus.OK, "Retrieved Subscription Details.", response);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @PreAuthorize("hasAuthority('ROLE_ROOT')")
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<SubscriptionDto>>> subscriptionsFilter(
            @RequestBody FilterRequest filterRequest){

        ResultDto<SubscriptionDto> resultDto = subscriptionService.fetchFIlterSubscription(filterRequest);
        return ResponseBuilder.success(HttpStatus.OK, "Subscriptions Fetched By Filter", resultDto);
    }

    @PreAuthorize("hasAuthority('ROLE_ROOT')")
    @GetMapping
    public ResponseEntity<ResponseStructure<ResultDto<SubscriptionDto>>> getAllSubscriptions(){
        ResultDto<SubscriptionDto> resultDto = subscriptionService.fetchAllSubscriptions();
        return ResponseBuilder.success(HttpStatus.OK, "Fetched All Subscriptions", resultDto);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ROOT', 'ROLE_ADMIN')")
    @GetMapping("/all/byAdmin")
    public ResponseEntity<ResponseStructure<ResultDto<SubscriptionDto>>> getAllSubscriptionByAdmin(
            @RequestParam String email
    ){
        ResultDto<SubscriptionDto> resultDto = subscriptionService.fetchAllScubscriptionByAdminEmail(email);
        return ResponseBuilder.success(HttpStatus.OK, "Fetched All Subscription of Admin Email : "+email, resultDto);
    }
}