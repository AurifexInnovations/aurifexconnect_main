package com.erp.Dto.Request;

import com.erp.Enum.TaxName;
import com.erp.Enum.TaxType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TaxRequest {

    private TaxName taxName;
    private TaxType taxType;
    private BigDecimal taxRate;
    private long id;
}
