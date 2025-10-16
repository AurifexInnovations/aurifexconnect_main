package com.erp.Exception.SameEmail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SameEmailFoundException extends RuntimeException {

    private String message;

}
