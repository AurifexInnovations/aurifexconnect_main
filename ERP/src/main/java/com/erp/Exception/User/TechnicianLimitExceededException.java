package com.erp.Exception.User;

public class TechnicianLimitExceededException extends RuntimeException {
  public TechnicianLimitExceededException(String message) {
    super(message);
  }
}
