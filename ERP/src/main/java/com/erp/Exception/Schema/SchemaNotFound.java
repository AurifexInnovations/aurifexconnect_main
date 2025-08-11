package com.erp.Exception.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SchemaNotFound extends RuntimeException {
   private String message;
}
