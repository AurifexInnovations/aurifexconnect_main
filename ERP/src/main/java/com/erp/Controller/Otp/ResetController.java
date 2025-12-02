package com.erp.Controller.Otp;

import com.erp.Service.Otp.RedisOtpService;
import com.erp.Service.Reset.OtpService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reset")
public class ResetController {
    private final OtpService otpService;
    private final RedisOtpService redisOtpService;

    @PostMapping("/password")
    @Operation(description = "API Endpoint to Generate OTP for Forget Password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP Sent Successfully"),
                    @ApiResponse(responseCode = "404", description = "Email Not Found")
            })
    public ResponseEntity<ResponseStructure<String>> resetPassword() {
        String response = redisOtpService.sendOtp();
        return ResponseBuilder.success(HttpStatus.OK, "OTP Sent Successfully", response);
    }

    @PostMapping("/verify")
    @Operation(description = "API Endpoint to Verify OTP",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP Verified Successfully"),
                    @ApiResponse(responseCode = "406", description = "Invalid OTP")
            })
    public ResponseEntity<ResponseStructure<String>> verifyOtp(@RequestParam String otp) {
        String response = redisOtpService.verifyReset(otp);
        return ResponseBuilder.success(HttpStatus.OK, "OTP Verified Successfully", response);
    }

    @PostMapping("/set")
    @Operation(description = "API Endpoint to Reset User Password After OTP Verification",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password Reset Successfully"),
                    @ApiResponse(responseCode = "400", description = "Password Mismatch or Invalid Request")
            })
    public ResponseEntity<ResponseStructure<String>> resetPassword(
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {

        String response = redisOtpService.resetPasswordReset(newPassword, confirmPassword);
        return ResponseBuilder.success(HttpStatus.OK, "Password Reset Successfully", response);
    }
}
