package com.erp.Controller.Otp;

import com.erp.Dto.Response.OtpResponseDTO;
import com.erp.Service.Otp.OtpServiceImpl;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/otp")
@RequiredArgsConstructor
@Slf4j
public class OtpController {


    private final OtpServiceImpl otpService;


    @GetMapping("/auth/token")
    public ResponseEntity<ResponseStructure<String>> getAuthToken() {
        log.info("Into [AuthController] [getAuthToken]");
        String token = otpService.getAuthToken();
        log.info("Exit [AuthController] [getAuthToken] :: token :: {}", token);
        return ResponseBuilder.success(HttpStatus.OK, "Token retrieved successfully", token);
    }

    @PostMapping("/send")
    public ResponseEntity<ResponseStructure<OtpResponseDTO>> sendOtp(
            @RequestParam String mobileNumber) {
        log.info("Into [OtpController] [sendOtp] :: mobileNumber={}", mobileNumber);

        OtpResponseDTO response = otpService.sendOtp(mobileNumber);

        log.info("Exit [OtpController] [sendOtp] :: mobileNumber={}", mobileNumber);
        return ResponseBuilder.success(HttpStatus.OK, "OTP sent successfully", response);
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseStructure<OtpResponseDTO>> validateOtp(
            @RequestParam String mobileNo,@RequestParam String code) {
        log.info("Into [OtpController] [validateOtp] :: code={}", code);

        OtpResponseDTO response = otpService.validateOtp(mobileNo,code);

        log.info("Exit [OtpController] [validateOtp] :: code={}", code);
        return ResponseBuilder.success(HttpStatus.OK, "OTP validated successfully", response);
    }



}
