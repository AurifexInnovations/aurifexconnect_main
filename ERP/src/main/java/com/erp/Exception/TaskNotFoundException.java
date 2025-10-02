package com.erp.Exception;


import lombok.Getter;

@Getter
public class TaskNotFoundException extends RuntimeException {

    private String message;

    public TaskNotFoundException(String message) {
        this.message = message;
    }
}
