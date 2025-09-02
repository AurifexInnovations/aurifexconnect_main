package com.erp.TechnicianApp.TechnicianException.Task;

public class TaskNotFoundById extends RuntimeException {
    public TaskNotFoundById(String message) {
        super(message);
    }
}
