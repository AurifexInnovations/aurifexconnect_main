package com.erp.Exception.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {

    private String message;

}