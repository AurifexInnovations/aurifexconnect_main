package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Param {
    private long id;
    private long userId;
    private String firstName;
    private String lastName;
}