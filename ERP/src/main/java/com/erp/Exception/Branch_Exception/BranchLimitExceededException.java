package com.erp.Exception.Branch_Exception;

public class BranchLimitExceededException extends RuntimeException {
    public BranchLimitExceededException(String message) {
        super(message);
    }
}
