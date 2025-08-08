package com.erp.Exception.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
public class AdminAlreadyExistsException extends RuntimeException {

    private String message;

}
