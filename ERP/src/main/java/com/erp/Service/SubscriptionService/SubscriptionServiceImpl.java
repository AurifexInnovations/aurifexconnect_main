package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.RazorpayRequest;
import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.RazorpayResponse;
import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import com.erp.Mapper.SubscriptionModule.SubscriptionMapper;
import com.erp.Model.PaymentEntity;
import com.erp.Model.SubscriptionEntity;
import com.erp.Repository.SubscriptionModule.PaymentRepository;
import com.erp.Repository.SubscriptionModule.SubscriptionRepository;
import com.erp.Utility.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements ISubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    RazorpayService razorpayService;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public SubscriptionDto fetchSubscriptionByUserId(String subscriptionId) {
        try {
            SubscriptionEntity entity = subscriptionRepository.findByUserId(subscriptionId).orElse(null);
            return entity != null ? SubscriptionMapper.toDto(entity) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserSubscriptionResponse createUserSubscription(UserSubscriptionRequest request) {

        if (request == null) {
            return null;
        }
        // Implementation logic to create a user subscription

        SubscriptionDto subscriptionDto = fetchSubscriptionByUserId(request.getUserId());
        if (subscriptionDto != null && subscriptionDto.getPlanEndDate() != null && subscriptionDto.getPlanEndDate().isAfter(java.time.LocalDate.now())) {
            UserSubscriptionResponse response = new UserSubscriptionResponse();
            // User already has a subscription

            response.setMessage("User already has a subscription.");
            return response;
        }

        //proceeding with new subscription payment
        RazorpayRequest razorpayRequest = new RazorpayRequest();
        razorpayRequest.setAmount(String.valueOf(request.getTotalAmount()));
        razorpayRequest.setCurrency("INR");
        String receiptId = request.getUserId() + "-" + System.currentTimeMillis();
        razorpayRequest.setReceipt(receiptId);
        razorpayRequest.setPayment_capture(true);

        RazorpayResponse razorpayResponse = razorpayService.create(razorpayRequest);

        PaymentEntity paymentEntity = new PaymentEntity();
        if (razorpayResponse != null) {
            paymentEntity.setPaymentId(Long.valueOf(razorpayResponse.getId()));
            paymentEntity.setPaymentStatus(razorpayResponse.getStatus());
            paymentEntity.setBranchCode(request.getBranchCode());
            paymentEntity.setBranchPlan(request.getPlanPeriodForBranches());
            paymentEntity.setTechnicianPlan(request.getPlanPeriodForTechnicians());
            paymentEntity.setActiveYn("Y");
        }

        PaymentEntity paymentEnt = paymentRepository.save(paymentEntity);

        if (!razorpayResponse.getStatus().equalsIgnoreCase("created")) {
            UserSubscriptionResponse response = new UserSubscriptionResponse();
            response.setMessage("Payment initiation failed.");
            return response;
        }

        if (paymentEnt != null && paymentEnt.getPaymentId() != null) {
            SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
            subscriptionEntity.setUserId(request.getUserId());
            subscriptionEntity.setSubscriptionPlan(request.getPlanPeriodForTechnicians());  //not sure taken randomly
            subscriptionEntity.setPlanPeriod(request.getPlanPeriodForBranches());
            subscriptionEntity.setPlanStartDate(LocalDate.now());
            subscriptionEntity.setPlanEndDate(LocalDate.parse(DateUtils.getFutureDate(String.valueOf(LocalDate.now())
                    , request.getPlanPeriodForBranches())));
            subscriptionEntity.setBranchCode(request.getBranchCode());
            subscriptionEntity.setCompanyCode(request.getCompanyCode());
            subscriptionEntity.setPaymentStatus(razorpayResponse.getStatus());
            subscriptionEntity.setPaymentId(paymentEnt.getPaymentId());
            subscriptionEntity.setActiveYn("Y");

            SubscriptionEntity savedEntity = subscriptionRepository.save(subscriptionEntity);

            if (savedEntity != null && savedEntity.getSubscriptionId() != null) {
                UserSubscriptionResponse response = new UserSubscriptionResponse();

                response.setMessage("Subscription created successfully.");
                response.setPlanEndDate(String.valueOf(savedEntity.getPlanEndDate()));

                return response;
            } else {
                UserSubscriptionResponse response = new UserSubscriptionResponse();
                response.setMessage("Failed to create subscription.");
                return response;
            }
        } else {
            UserSubscriptionResponse response = new UserSubscriptionResponse();
            response.setMessage("Failed to save payment details.");
            return response;
        }
    }
}