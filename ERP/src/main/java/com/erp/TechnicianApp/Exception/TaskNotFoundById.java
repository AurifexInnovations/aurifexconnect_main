package com.erp.TechnicianApp.Exception;

public class TaskNotFoundById extends RuntimeException {
    public TaskNotFoundById(String message) {
        super(message);
    }
}
