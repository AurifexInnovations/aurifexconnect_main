package com.erp.Controller.StockTransfer;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StockTransferResponse;
import com.erp.Service.StockTransferService.StockTransferService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import com.erp.Utility.SimpleErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Stock Transfer Controller", description = "APIs for Stock Transfer operations between branches")
public class StockTransferController {

    private final StockTransferService stockTransferService;

    @PostMapping("stocktransfer")
    @Operation(description = "API to Create a New Stock Transfer",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Stock Transfer Request Created")
            })
    public ResponseEntity<ResponseStructure<StockTransferResponse>> createTransfer(@Valid @RequestBody StockTransferRequest request) {
        StockTransferResponse response = stockTransferService.createStockTransfer(request);
        return ResponseBuilder.success(HttpStatus.CREATED, "Stock Transfer Created", response);
    }

    @PostMapping("stocktransfer/approve")
    @Operation(description = "API to Approve a Stock Transfer Request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transfer Approved Successfully"),
                    @ApiResponse(responseCode = "404", description = "Transfer Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<StockTransferResponse>> approveTransfer(@RequestBody TransferActionRequest request) {
        StockTransferResponse response = stockTransferService.approveTransfer(request);
        return ResponseBuilder.success(HttpStatus.OK, "Transfer Approved", response);
    }

    @PostMapping("update/stock-status")
    @Operation(description = "API to Reject a Stock Transfer Request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transfer Rejected Successfully"),
                    @ApiResponse(responseCode = "404", description = "Transfer Not Found", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ResponseStructure<StockTransferResponse>> rejectTransfer(@RequestBody TransferActionRequest request) {
        StockTransferResponse response = stockTransferService.rejectTransfer(request);
        return ResponseBuilder.success(HttpStatus.OK, "Transfer Rejected", response);
    }

    @PostMapping("stocktransfer/all")
    @Operation(description = "API to Fetch All Stock Transfers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All Stock Transfers Retrieved Successfully")
            })
    public ResponseEntity<ListResponseStructure<StockTransferResponse>> getAllTransfers(@RequestBody PaginationRequest request) {
        List<StockTransferResponse> responses = stockTransferService.getAllTransfers(request);
        return ResponseBuilder.success(HttpStatus.OK, "All Transfers Retrieved Successfully", responses);
    }

    @PostMapping("stocktransfer/by-status")
    @Operation(description = "API to Fetch Stock Transfers by Status (PENDING/APPROVED/REJECTED)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transfers Retrieved by Status"),
                    @ApiResponse(responseCode = "400", description = "Invalid Status", content = {
                            @Content(schema = @Schema(implementation = SimpleErrorResponse.class))
                    })
            })
    public ResponseEntity<ListResponseStructure<StockTransferResponse>> getTransfersByStatus(@RequestBody StockTransferParam param) {
        List<StockTransferResponse> responses = stockTransferService.getTransfersByStatus(param);
        return ResponseBuilder.success(HttpStatus.OK, "Transfers by Status Retrieved Successfully", responses);
    }

    @PostMapping("stocktransfer/filter")
    public ResponseEntity<ResponseStructure<ResultDto<StockTransferResponse>>> getStockTransferDetails(@RequestBody FilterRequest filterRequest)
    {
        ResultDto<StockTransferResponse> responses = stockTransferService.getStockTransferDetails(filterRequest);

        return ResponseBuilder.success(HttpStatus.OK, "All Stock Transfers Fetched", responses);
    }

    @PutMapping("/update-status")
    public ResponseEntity<String> updateStatus(@RequestBody UpdateStockTransferStatusRequest request) {
        stockTransferService.updateStockTransferStatus(request.getId(), request.getStatus());
        return ResponseEntity.ok("Status updated successfully");
    }

}
