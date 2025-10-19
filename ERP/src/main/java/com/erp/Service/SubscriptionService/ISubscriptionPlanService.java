package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.UserSubscriptionResponse;
import jakarta.validation.Valid;

public interface ISubscriptionPlanService {
    UserSubscriptionResponse createUserSubscription(UserSubscriptionRequest request);

    UserSubscriptionResponse fetchUserSubscription(String userId);

    String checkPlanValidity(String userId);
}
