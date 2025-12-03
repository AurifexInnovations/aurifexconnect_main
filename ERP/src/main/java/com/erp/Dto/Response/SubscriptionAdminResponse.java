package com.erp.Dto.Response;

import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionAdminResponse {
    private SubscriptionDto subscriptionDto;
    private AdminResponse adminResponse;
}
