package com.erp.Controller.Auth;

import com.erp.Dto.Request.AuthRecord;
import com.erp.Dto.Request.LoginRequest;
import com.erp.Service.Auth.AuthService;
import com.erp.Service.TokenGeneration.TokenGenerationService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenGenerationService tokenGenerationService;

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthRecord>> login(@RequestBody LoginRequest loginRequest) {
        AuthRecord authRecord = authService.login(loginRequest);
        HttpHeaders headers = tokenGenerationService.grantAccessAndRefreshToken(authRecord);
        return ResponseBuilder.success(HttpStatus.OK, headers, "Login successful", authRecord);
    }

    @PostMapping("/refresh-login")
    public ResponseEntity<ResponseStructure<AuthRecord>> refreshLogin(@CookieValue("rt") String refreshToken) {
        AuthRecord authRecord = authService.refreshLogin(refreshToken);
        HttpHeaders headers = tokenGenerationService.grantAccessAndRefreshToken(authRecord);
        return ResponseBuilder.success(HttpStatus.OK, headers, "New access token generated", authRecord);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseStructure<AuthRecord>> logout(
            @CookieValue("rt") String refreshToken,
            @CookieValue("at") String accessToken) {
        HttpHeaders headers = authService.logout(refreshToken, accessToken);
        return ResponseBuilder.success(HttpStatus.OK, headers, "Logout successful");
    }
}