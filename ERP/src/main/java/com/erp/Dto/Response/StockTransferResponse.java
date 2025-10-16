package com.erp.Dto.Response;
import com.erp.Enum.StockTransferStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StockTransferResponse {

    private long id;

    private String fromBranchName;
    private String toBranchName;
    private String itemName;

    private double quantity;

    private StockTransferStatus status;

    private String approvedBy;

    private LocalDateTime createdAt;
}
