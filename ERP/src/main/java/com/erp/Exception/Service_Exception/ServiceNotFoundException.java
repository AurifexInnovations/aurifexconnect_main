package com.erp.Exception.Service_Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ServiceNotFoundException extends RuntimeException
{
    public ServiceNotFoundException(String message)
    {
        super(message);
    }
}
