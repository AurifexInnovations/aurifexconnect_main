package com.erp.Exception.Service_Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceNotFoundByIdException extends RuntimeException {
    private final String message ;
}
