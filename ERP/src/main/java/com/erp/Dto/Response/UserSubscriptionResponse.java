package com.erp.Dto.Response;

import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSubscriptionResponse {

    private String message;
    private String PlanEndDate;
    private SubscriptionDto subscriptionDto;
}
