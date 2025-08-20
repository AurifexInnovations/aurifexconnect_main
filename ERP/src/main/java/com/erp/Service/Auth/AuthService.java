package com.erp.Service.Auth;

import com.erp.Dto.Request.AuthRecord;
import com.erp.Dto.Request.LoginRequest;
import com.erp.Model.GenericUser;
import org.springframework.http.HttpHeaders;

public interface AuthService {

    AuthRecord login(LoginRequest loginRequest);

    AuthRecord refreshLogin(String refreshToken);

    HttpHeaders logout(String refreshToken, String accessToken);
}