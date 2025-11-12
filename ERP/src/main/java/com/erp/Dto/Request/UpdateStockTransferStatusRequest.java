package com.erp.Dto.Request;

import com.erp.Enum.StockTransferStatus;
import lombok.Data;

@Data
public class UpdateStockTransferStatusRequest {
    private long id;
    private StockTransferStatus status;
}
