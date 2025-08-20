package com.erp.Exception.TaskActivity;

import lombok.Getter;

@Getter
public class TaskActivityNotFoundException extends RuntimeException {

    public TaskActivityNotFoundException(String message) {
        super(message);
    }
}