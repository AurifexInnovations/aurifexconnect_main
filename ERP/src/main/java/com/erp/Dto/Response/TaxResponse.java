package com.erp.Dto.Response;

import com.erp.Enum.TaxName;
import com.erp.Enum.TaxType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaxResponse {

    private long id;
    private TaxName taxName;
    private TaxType taxType;
    private BigDecimal taxRate;
    private LocalDateTime createdAt;
}
