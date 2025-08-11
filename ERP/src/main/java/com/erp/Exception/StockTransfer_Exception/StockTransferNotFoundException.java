package com.erp.Exception.StockTransfer_Exception;

import lombok.Getter;

@Getter
public class StockTransferNotFoundException extends RuntimeException {
  public StockTransferNotFoundException(String message) {
    super(message);
  }
}
