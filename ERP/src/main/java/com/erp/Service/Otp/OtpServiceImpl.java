package com.erp.Service.Otp;

import com.erp.Dto.Response.OtpResponseDTO;

import com.erp.Dto.Response.UserOtpDTO;
import com.erp.Exception.RequestNotFoundException;
import com.erp.Model.UserOtp;
import com.erp.Repository.Otp.UserOtpRepository;
import com.erp.Thirdparty.Otp.OtpCpassThirdpartyCallerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpServiceImpl  implements  OtpService{


    private final OtpCpassThirdpartyCallerService authClient;

    private final UserOtpRepository userOtpRepository;


    @Value("${otp.customer-id}")
    private String customerId;

    @Value("${otp.key}")
    private String key;

    @Value("${otp.scope}")
    private String scope;

    @Value("${otp.country}")
    private String country;

    @Value("${otp.email}")
    private String email;

    @Value("${otp.flowType}")
    private String flowType;


    @Override
    public String getAuthToken() {
        log.info("Entering [getAuthToken] with parameters :: customerId={}, scope={}, country={}, email={}",
                customerId, scope, country, email);

        try {
            ResponseEntity<String> response = authClient.getAuthToken(customerId, key, scope, country, email);
            log.debug("Received response from authClient :: Status={}, Body={}",
                    response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Auth token fetched successfully for customerId={}", customerId);
                return response.getBody();
            } else {
                log.error("Failed to fetch auth token :: Status={}, Response={}",
                        response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to fetch auth token. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching auth token for customerId={} :: {}",
                    customerId, e.getMessage(), e);
            throw new RuntimeException("Error occurred while fetching auth token", e);
        }
    }

    public OtpResponseDTO sendOtp(String mobileNumber) {
        log.info("Sending OTP to mobile number={}", mobileNumber);

        if (mobileNumber == null || mobileNumber.isEmpty()) {
            throw new RequestNotFoundException("Mobile number is required to validate OTP.");
        }

        try {
            String authToken = getAuthToken();

            ResponseEntity<OtpResponseDTO> response = authClient.sendOtp(authToken, country,flowType, mobileNumber);


            log.debug("CPaaS Response :: Status={}, Body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("OTP sent successfully to {}", mobileNumber);
                OtpResponseDTO otpResponse = response.getBody();
                if (otpResponse != null && otpResponse.getData() != null) {

                    UserOtpDTO data = otpResponse.getData();

                    UserOtp model = userOtpRepository.findByMobileNo(data.getMobileNumber())
                            .orElse(new UserOtp());


                    model.setVerificationId(data.getVerificationId());
                    model.setMobileNo(data.getMobileNumber());
                    model.setOtpGeneratedDateTime(LocalDateTime.now());
                    model.setOtpExpiryDateTime(LocalDateTime.now().plusMinutes(5));
                    model.setIsVerified(false);
                    if (model.getId() != null) {
                        model.setMaxAttemp(model.getMaxAttemp() != null ? model.getMaxAttemp() + 1 : 1);
                    } else {
                        model.setMaxAttemp(0);
                    }
                    userOtpRepository.save(model);
                }
                return response.getBody();
            } else {
                log.error("Failed to send OTP :: Status={}, Body={}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to send OTP. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Exception while sending OTP to {} :: {}", mobileNumber, e.getMessage(), e);
            throw new RuntimeException("Error sending OTP", e);
        }
    }

    public OtpResponseDTO validateOtp(String verificationId,String code) {
        log.info("Validating OTP for code={}", code);
        try {


            if (verificationId == null || verificationId.isEmpty()) {
                throw new RequestNotFoundException("No OTP request found. Please request OTP first.");
            }

            String authToken = getAuthToken();

            ResponseEntity<OtpResponseDTO> response = authClient.validateOtp(authToken, verificationId, code, flowType);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("OTP validated successfully for verificationId={}", verificationId);
                return response.getBody();
            } else {
                log.error("Failed to validate OTP :: Status={}, Body={}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to validate OTP. Status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Exception while validating OTP for code={} :: {}", code, e.getMessage(), e);
            throw new RuntimeException("Error validating OTP", e);
        }
    }
}
