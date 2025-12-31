package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmcRequestDto {

    private Long amcId;
    private String amcStatus;
    private String pauseReason;
    private String terminationReason;

}
