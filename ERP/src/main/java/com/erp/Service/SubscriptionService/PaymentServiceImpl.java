package com.erp.Service.SubscriptionService;

import com.erp.Dto.SubscriptionsDto.PaymentDto;
import com.erp.Mapper.SubscriptionModule.PaymentMapper;
import com.erp.Model.PaymentEntity;
import com.erp.Repository.SubscriptionModule.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentDto fetchPaymentById(Long paymentId) {
        try {
            PaymentEntity entity = paymentRepository.findByPaymentId(paymentId).orElse(null);
            return entity != null ? PaymentMapper.toDto(entity) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}