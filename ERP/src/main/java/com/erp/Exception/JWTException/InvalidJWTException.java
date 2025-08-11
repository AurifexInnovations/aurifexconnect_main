package com.erp.Exception.JWTException;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidJWTException extends RuntimeException {
    private final String message;
}
