package com.erp.Service.Otp;

import com.erp.Dto.Response.OtpResponseDTO;

public interface OtpService {

    String getAuthToken();

    public OtpResponseDTO sendOtp(String mobileNumber);

    public OtpResponseDTO validateOtp(String verificationId,String code);

}
