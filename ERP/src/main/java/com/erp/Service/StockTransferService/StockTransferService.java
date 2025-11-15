package com.erp.Service.StockTransferService;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StockTransferResponse;
import com.erp.Enum.StockTransferStatus;

import java.util.List;

public interface StockTransferService {

    void createBulkStockTransfer(List<StockTransferRequest> requests);


    StockTransferResponse approveTransfer(TransferActionRequest request);

    StockTransferResponse rejectTransfer(TransferActionRequest request);

    List<StockTransferResponse> getAllTransfers(PaginationRequest request);

    List<StockTransferResponse> getTransfersByStatus(StockTransferParam param);

    ResultDto<StockTransferResponse> getStockTransferDetails(FilterRequest filterRequest);

    public void updateStockTransferStatus(long id, StockTransferStatus status);

}
