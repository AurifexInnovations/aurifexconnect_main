package com.erp.Thirdparty.Otp;

import com.erp.Dto.Response.OtpResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
@Slf4j
public class OtpCpassThirdpartyCallerService {

    @Value("${otp.url}")
    private String BASE_URL;

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Get Auth Token
     */
    public ResponseEntity<String> getAuthToken(String customerId, String base64EncryptedKey,
                                               String scope, String country, String email) {
        String url = BASE_URL + "/auth/v1/authentication/token";

        // ✅ Encode the Base64 key safely
        String encodedKey = URLEncoder.encode(base64EncryptedKey, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("customerId", customerId)
                .queryParam("key", encodedKey)
                .queryParam("scope", scope)
                .queryParam("country", country)
                .queryParam("email", email);

        log.info("Final URL: {}", builder.toUriString());

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );
    }



    /**
     * Send OTP
     */
    public ResponseEntity<OtpResponseDTO> sendOtp(String authToken, String countryCode,
                                                  String flowType, String mobileNumber) {
        String url = BASE_URL + "/verification/v3/send";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("countryCode", countryCode)
                .queryParam("flowType", flowType)
                .queryParam("mobileNumber", mobileNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.set("authToken", authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                OtpResponseDTO.class
        );
    }

    /**
     * Validate OTP
     */
    public ResponseEntity<OtpResponseDTO> validateOtp(String authToken, String verificationId,
                                                      String code, String flowType) {
        String url = BASE_URL + "/verification/v3/validateOtp";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("verificationId", verificationId)
                .queryParam("code", code)
                .queryParam("flowType", flowType);

        HttpHeaders headers = new HttpHeaders();
        headers.set("authToken", authToken);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                OtpResponseDTO.class
        );
    }
}
