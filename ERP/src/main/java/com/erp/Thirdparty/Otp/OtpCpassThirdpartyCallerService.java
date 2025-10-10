package com.erp.Thirdparty.Otp;

import com.erp.Dto.Response.OtpResponseDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OtpCpassThirdpartyCallerService {

    private static final String BASE_URL = "https://cpaas.messagecentral.com";

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Get Auth Token
     */
    public ResponseEntity<String> getAuthToken(String customerId, String base64EncryptedKey,
                                               String scope, String country, String email) {
        String url = BASE_URL + "/auth/v1/authentication/token";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("customerId", customerId)
                .queryParam("key", base64EncryptedKey)
                .queryParam("scope", scope)
                .queryParam("country", country)
                .queryParam("email", email);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
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
