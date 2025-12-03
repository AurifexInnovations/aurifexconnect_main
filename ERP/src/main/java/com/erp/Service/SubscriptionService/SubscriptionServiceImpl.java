package com.erp.Service.SubscriptionService;

import com.erp.CustomRepository.SubscriptionCustomRepository;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UserSubscriptionRequest;
import com.erp.Dto.Response.AdminResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SubscriptionAdminResponse;
import com.erp.Dto.Response.UserSubscriptionResponse;
import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import com.erp.Exception.Admin.AdminNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Admin.AdminMapper;
import com.erp.Mapper.SubscriptionModule.SubscriptionMapper;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Model.Admin;
import com.erp.Model.SubscriptionEntity;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Admin.AdminUserRepositoryCustom;
import com.erp.Repository.Admin.AdminUserRepositoryImpl;
import com.erp.Repository.SubscriptionModule.SubscriptionRepository;
import com.erp.Security.util.UserIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements ISubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionCustomRepository subscriptionCustomRepository;
    @Autowired
    private UserIdentity userIdentity;
    @Autowired
    private MetaAdminRepository metaAdminRepository;
    @Autowired
    private AdminUserRepositoryImpl adminUserRepositoryCustom;
    @Autowired
    private AdminMapper adminMapper;

//    @Autowired
//    RazorpayService razorpayService;

//    @Autowired
//    PaymentRepository paymentRepository;

    @Override
    public SubscriptionDto fetchSubscriptionByUserId(String userId) {
        try {
            SubscriptionEntity entity = subscriptionRepository.findByUserIdAndActiveYn(userId, "Y").orElse(null);
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

        SubscriptionDto subscriptionDto = fetchSubscriptionByUserId(userIdentity.getCurrentUserEmail());
        if (subscriptionDto != null && subscriptionDto.getPlanEndDate() != null && subscriptionDto.getPlanEndDate().isAfter(java.time.LocalDate.now())) {
            UserSubscriptionResponse response = new UserSubscriptionResponse();
            // User already has a subscription

            response.setMessage("User already has a subscription.");
            response.setPlanEndDate(String.valueOf(subscriptionDto.getPlanEndDate()));

            return response;
        }

//        //proceeding with new subscription payment
//        RazorpayRequest razorpayRequest = new RazorpayRequest();
//        razorpayRequest.setAmount(String.valueOf(request.getTotalAmount()));
//        razorpayRequest.setCurrency("INR");
//        String receiptId = request.getUserId() + "-" + System.currentTimeMillis();
//        razorpayRequest.setReceipt(receiptId);
//        razorpayRequest.setPayment_capture(true);

//        RazorpayResponse razorpayResponse = razorpayService.create(razorpayRequest);

//        PaymentEntity paymentEntity = new PaymentEntity();
//        if (razorpayResponse != null) {
//            paymentEntity.setPaymentId(Long.valueOf(razorpayResponse.getId()));
//            paymentEntity.setPaymentStatus(razorpayResponse.getStatus());
//            paymentEntity.setBranchCode(request.getBranchCode());
//            paymentEntity.setBranchPlan(request.getPlanPeriodForBranches());
//            paymentEntity.setTechnicianPlan(request.getPlanPeriodForTechnicians());
//            paymentEntity.setActiveYn("Y");
//        }
//
//        PaymentEntity paymentEnt = paymentRepository.save(paymentEntity);

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setUserId(userIdentity.getCurrentUserEmail());
        subscriptionEntity.setPlanStartDate(LocalDate.now());

        if (request.getPlanPeriod().equalsIgnoreCase("MONTHLY")) {
            subscriptionEntity.setPlanEndDate(LocalDate.now().plusMonths(1));
        } else if (request.getPlanPeriod().equalsIgnoreCase("QUARTERLY")) {
            subscriptionEntity.setPlanEndDate(LocalDate.now().plusMonths(3));
        } else if (request.getPlanPeriod().equalsIgnoreCase("YEARLY")) {
            subscriptionEntity.setPlanEndDate(LocalDate.now().plusYears(1));
        } else {
            // Default to year if plan period is unrecognized
            subscriptionEntity.setPlanEndDate(LocalDate.now().plusYears(1));
        }

        subscriptionEntity.setAccountUser(String.valueOf(request.getAccountUser()));
        subscriptionEntity.setTotalAmount(String.valueOf(request.getTotalAmount()));
        subscriptionEntity.setTotalBranches(String.valueOf(request.getTotalBranches()));
        subscriptionEntity.setTotalTechnicians(String.valueOf(request.getTotalTechnicians()));

        subscriptionEntity.setPlanPeriod(request.getPlanPeriod());
        subscriptionEntity.setBranchCode(request.getBranchCode());
        subscriptionEntity.setCompanyCode(request.getCompanyCode());
        subscriptionEntity.setPaymentStatus(request.getPaymentStatus());
        subscriptionEntity.setPaymentTransactionId(request.getPaymentTransactionId());
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
    }

    @Override
    public ResultDto<SubscriptionDto> fetchFIlterSubscription(FilterRequest filterRequest) {
        ResultDto<SubscriptionDto> res = subscriptionCustomRepository.getSubscriptionsFilter(filterRequest);
        return res;
    }

    @Override
    public ResultDto<SubscriptionDto> fetchAllSubscriptions() {
        List<SubscriptionDto> list = SubscriptionMapper.toSubscriptionDtoList(subscriptionRepository.findAll());

        ResultDto<SubscriptionDto> result = new ResultDto<>();

        result.setCount(list.size());
        result.setResults(list);

        return result;
    }

    @Override
    public SubscriptionAdminResponse fetchSubscriptionAdminByUserId(String userId) {
        SubscriptionAdminResponse subscriptionAdminResponse = new SubscriptionAdminResponse();

        String schema = metaAdminRepository.findSchemaNameByAdminEmail(userId)
                .orElseThrow(() -> new AdminNotFoundException("First Create Admin !!"));

        SubscriptionEntity subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription Not Found For Email : "+userId));

        Admin admin = adminUserRepositoryCustom.findByEmailWithSchema(userId, schema)
                .orElseThrow(() -> new AdminNotFoundException("First Create Admin !!"));

        subscriptionAdminResponse.setSubscriptionDto(SubscriptionMapper.toDto(subscription));
        subscriptionAdminResponse.setAdminResponse(adminMapper.mapToAdminResponse(admin));

        return subscriptionAdminResponse;
    }

    @Override
    public ResultDto<SubscriptionDto> fetchAllScubscriptionByAdminEmail(String email) {
        List<SubscriptionEntity> list = subscriptionRepository.findAllByUserIdOrderByCreatedAtAsc(email);
        List<SubscriptionDto> result = SubscriptionMapper.toSubscriptionDtoList(list);

        ResultDto<SubscriptionDto> resultDto = new ResultDto<>();
        resultDto.setResults(result);
        resultDto.setCount(result.size());
        return resultDto;
    }
}