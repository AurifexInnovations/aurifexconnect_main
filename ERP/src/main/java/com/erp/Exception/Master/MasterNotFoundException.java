package com.erp.Exception.Master;

import lombok.Getter;

@Getter
public class MasterNotFoundException extends RuntimeException {

  private String message;

    public MasterNotFoundException(String message) {
      this.message = message;
    }
}
