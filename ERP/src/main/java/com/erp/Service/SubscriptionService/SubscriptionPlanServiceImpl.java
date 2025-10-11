package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Model.Subscription;
import com.erp.Repository.Subscribe.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionPlanServiceImpl implements ISubscriptionPlanService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
    public UserSubscriptionResponse createUserSubscription(UserSubscriptionRequest request) {
        UserSubscriptionResponse userSubscriptionResponse = new UserSubscriptionResponse();

        if (request == null)
            return null;

        Subscription subscription = new Subscription();
//        subscription.setSubscriptionPlan();

        subscriptionRepository.save(subscription);
        return userSubscriptionResponse;
    }

    @Override
    public UserSubscriptionResponse fetchUserSubscription(String userId) {
//        UserSubscriptionResponse userSubscriptionResponse = new UserSubscriptionResponse();
//        Subscription subscription = new Subscription();
//
//        if (userId != null) {
//            subscription = subscriptionRepository.findByUserId(userId);
//
//            userSubscriptionResponse.setUserId(subscription.getUserId());
//            userSubscriptionResponse.setPlanStartDate(subscription.getPlanStartDate());
//            userSubscriptionResponse.setPlanEndDate(subscription.getPlanEndDate());
//            userSubscriptionResponse.setAmount(subscription.getAmount());
//        } else
            return null;
//        return userSubscriptionResponse;
    }

    @Override
    public String checkPlanValidity(String userId) {
        if (userId != null) {
            Subscription subscription = subscriptionRepository.findByUserId(userId);
            if (subscription != null) {
                LocalDate currentDate = LocalDate.now();
                LocalDate planEndDate = subscription.getPlanEndDate().toLocalDate();
                if (planEndDate.isAfter(currentDate)) {
                    return "user subscription is valid";
                } else {
                    return "user subscription not valid";
                }
            } else {
                return "user not subscribed";
            }
        } else
            return "user id is null";
    }
}
