package com.erp.Service.StockTransferService;

import com.erp.Dto.Request.PaginationRequest;
import com.erp.Dto.Request.StockTransferParam;
import com.erp.Dto.Request.StockTransferRequest;
import com.erp.Dto.Request.TransferActionRequest;
import com.erp.Dto.Response.StockTransferResponse;

import java.util.List;

public interface StockTransferService {

    StockTransferResponse createStockTransfer(StockTransferRequest request);

    StockTransferResponse approveTransfer(TransferActionRequest request);

    StockTransferResponse rejectTransfer(TransferActionRequest request);

    List<StockTransferResponse> getAllTransfers(PaginationRequest request);

    List<StockTransferResponse> getTransfersByStatus(StockTransferParam param);

}
