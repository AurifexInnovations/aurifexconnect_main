package com.erp.AuthAPITest;

import com.erp.Config.AppEnv;
import com.erp.Dto.Request.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Table;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppEnv env;



    private String accessToken;
    private String refreshToken;

    @BeforeAll
     void loginAndStoreCookies() throws Exception {

        String baseUrl = env.getBaseUrl();

        LoginRequest loginRequest = new LoginRequest("vishal369@gmail.com", "Vishal@369","");


        MockHttpServletResponse response = mockMvc.perform(post(baseUrl+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isCreated())
                .andExpect(cookie().exists("at"))
                .andExpect(cookie().exists("rt"))
                .andExpect(jsonPath("$.message").value("login"))
                .andReturn()
                .getResponse();

        accessToken = response.getCookie("at").getValue();
        refreshToken = response.getCookie("rt").getValue();
    }

    @Test
    @Order(1)
    void testRefresh_withStoredRefreshToken_shouldReturnNewAccessToken() throws Exception {

        String baseUrl = env.getBaseUrl();

        Cookie rtCookie = new Cookie("rt", refreshToken);

        mockMvc.perform(post(baseUrl+"/refresh")
                        .cookie(rtCookie))
                .andExpect(status().isCreated())
                .andExpect(cookie().exists("at"))
                .andExpect(jsonPath("$.message").value("New access token generated !!"));
    }

    @Test
    @Order(2)
    void testLogout_withStoredCookies_shouldClearTokens() throws Exception {

        String baseUrl = env.getBaseUrl();

        Cookie atCookie = new Cookie("at", accessToken);
        Cookie rtCookie = new Cookie("rt", refreshToken);

        mockMvc.perform(post(baseUrl+"/logout")
                        .cookie(atCookie, rtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout Successfully !!"))
                .andExpect(cookie().maxAge("at", 0))
                .andExpect(cookie().maxAge("rt", 0));
    }


}
