package com.erp.Controller.Otp;

import com.erp.Service.Otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/otp")
@RequiredArgsConstructor
public class OtpController {


    private final OtpService otpService;

    @PostMapping("/generate/mobileNo")
    public void generateOtp(@RequestParam("mNo")  String mobileNo){

    }

}
