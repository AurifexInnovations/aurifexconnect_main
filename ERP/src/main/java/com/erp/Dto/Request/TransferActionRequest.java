package com.erp.Dto.Request;

import com.erp.Enum.StockTransferStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferActionRequest {
    private long transferId;
    private String approverName;
    private StockTransferStatus status;
}
