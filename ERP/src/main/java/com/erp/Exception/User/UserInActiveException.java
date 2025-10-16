package com.erp.Exception.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInActiveException extends RuntimeException {
   private String message;
}
