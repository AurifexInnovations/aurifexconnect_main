package com.erp.Security.Filter;

import com.erp.Security.JWT.TokenType;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterHelper {
        public static String extractTokenFromCookie(Cookie[] cookies, TokenType tokenType) {
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    log.info("Found cookie with name: {} & value: {}", cookie.getName(), cookie.getValue());
                    if (cookie.getName().equals(tokenType.type())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            return token;
        }
    }
