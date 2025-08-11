package com.erp.Dto.Request;

import com.erp.Enum.StockTransferStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockTransferParam {
    private StockTransferStatus status;
}
