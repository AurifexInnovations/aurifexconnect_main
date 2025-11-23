package com.erp.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillStatusUpdateRequestDTO {

    @NotNull
    private String outstandingPayablesStatus;
}
