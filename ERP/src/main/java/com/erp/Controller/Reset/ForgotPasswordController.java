package com.erp.Controller.Reset;

import com.erp.Dto.Response.OtpResponseDTO;
import com.erp.Service.Reset.OtpService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class ForgotPasswordController {

    private final OtpService otpService;

    @PostMapping("/forgot")
    @Operation(description = "API Endpoint to Generate OTP for Forget Password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP Sent Successfully"),
                    @ApiResponse(responseCode = "404", description = "Email Not Found")
            })
    public ResponseEntity<ResponseStructure<String>> forgotPassword(@RequestParam String email) {
        String response = otpService.sendOtp(email);
        return ResponseBuilder.success(HttpStatus.OK, "OTP Sent Successfully", response);
    }

    @PostMapping("/verify")
    @Operation(description = "API Endpoint to Verify OTP",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP Verified Successfully"),
                    @ApiResponse(responseCode = "406", description = "Invalid OTP")
            })
    public ResponseEntity<ResponseStructure<String>> verifyOtp(@RequestParam String email,
                                                               @RequestParam String otp) {
        String response = otpService.verifyOtp(email, otp);
        return ResponseBuilder.success(HttpStatus.OK, "OTP Verified Successfully", response);
    }

    @PostMapping("/reset")
    @Operation(description = "API Endpoint to Reset User Password After OTP Verification",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password Reset Successfully"),
                    @ApiResponse(responseCode = "400", description = "Password Mismatch or Invalid Request")
            })
    public ResponseEntity<ResponseStructure<String>> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {

        String response = otpService.resetPassword(email, newPassword, confirmPassword);
        return ResponseBuilder.success(HttpStatus.OK, "Password Reset Successfully", response);
    }
}
