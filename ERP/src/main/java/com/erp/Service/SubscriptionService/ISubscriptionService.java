package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Dto.SubscriptionsDto.SubscriptionDto;

public interface ISubscriptionService {
    SubscriptionDto fetchSubscriptionByUserId(String userId);

    UserSubscriptionResponse createUserSubscription(UserSubscriptionRequest request);

    ResultDto<SubscriptionDto> fetchFIlterSubscription(FilterRequest filterRequest);
}