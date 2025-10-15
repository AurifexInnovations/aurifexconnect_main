package com.erp.Exception.Branch_Exception;

import lombok.Getter;

@Getter
public class BranchNotFoundException extends RuntimeException
{
    public BranchNotFoundException(String message)
    {
        super(message);
    }
}
