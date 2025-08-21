package com.erp.TechnicianApp.Exception;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(String message){
        super(message);
    }
}
