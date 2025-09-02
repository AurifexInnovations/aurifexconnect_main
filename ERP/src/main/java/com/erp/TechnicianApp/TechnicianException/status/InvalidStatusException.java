package com.erp.TechnicianApp.TechnicianException.status;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(String message){
        super(message);
    }
}