package com.erp.Service.SubscriptionService;

import com.erp.Dto.SubscriptionsDto.PaymentDto;

public interface IPaymentService {
    PaymentDto fetchPaymentById(Long paymentId);
}