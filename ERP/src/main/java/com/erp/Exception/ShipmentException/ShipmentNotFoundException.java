package com.erp.Exception.ShipmentException;

import lombok.Getter;

@Getter
public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(String message) {
        super(message);
    }
}
