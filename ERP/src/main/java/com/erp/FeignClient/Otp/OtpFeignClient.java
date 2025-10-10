package com.erp.FeignClient.Otp;

import com.erp.Dto.Response.OtpResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "OtpFeignClient",
        url = "https://cpaas.messagecentral.com"
)
public interface OtpFeignClient {

    //token OTP
    @GetMapping("/auth/v1/authentication/token")
    ResponseEntity<String> getAuthToken(
            @RequestParam("customerId") String customerId,
            @RequestParam("key") String base64EncryptedKey,
            @RequestParam("scope") String scope,
            @RequestParam("country") String country,
            @RequestParam("email") String email
    );

    // Send OTP
    @PostMapping("/verification/v3/send")
    ResponseEntity<OtpResponseDTO> sendOtp(
            @RequestHeader("authToken") String authToken,
            @RequestParam("countryCode") String countryCode,
            @RequestParam("flowType") String flowType,
            @RequestParam("mobileNumber") String mobileNumber
    );

    // Validate OTP
    @GetMapping(value = "/verification/v3/validateOtp")
    ResponseEntity<OtpResponseDTO> validateOtp(
            @RequestHeader("authToken") String authToken,
            @RequestParam("verificationId") String verificationId,
            @RequestParam("code") String code,
            @RequestParam("flowType") String flowType
    );
}
