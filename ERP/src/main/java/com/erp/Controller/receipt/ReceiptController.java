package com.erp.Controller.receipt;

import com.erp.Dto.Request.ReceiptRequestDto;
import com.erp.Dto.Response.ReceiptResponseDto;
import com.erp.Service.receipt.ReceiptService;

import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    // ========================
    // CREATE
    // ========================

    @PostMapping
    @Operation(
        description = "API Endpoint to Create a New Receipt",
        responses = {
            @ApiResponse(responseCode = "201", description = "Receipt Created Successfully")
        }
    )
    public ResponseEntity<ResponseStructure<ReceiptResponseDto>> createReceipt(
            @Valid @RequestBody ReceiptRequestDto requestDto
    ) {
        ReceiptResponseDto response = receiptService.create(requestDto);
        return ResponseBuilder.success(
                HttpStatus.CREATED,
                "Receipt Created Successfully",
                response
        );
    }

    // ========================
    // UPDATE
    // ========================

    @PutMapping("/{id}")
    @Operation(
        description = "API Endpoint to Update Receipt",
        responses = {
            @ApiResponse(responseCode = "200", description = "Receipt Updated Successfully")
        }
    )
    public ResponseEntity<ResponseStructure<ReceiptResponseDto>> updateReceipt(
            @PathVariable Long id,
            @Valid @RequestBody ReceiptRequestDto requestDto
    ) {
        ReceiptResponseDto response = receiptService.update(id, requestDto);
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Receipt Updated Successfully",
                response
        );
    }

    // ========================
    // GET ALL
    // ========================

    @GetMapping
    @Operation(
        description = "API Endpoint to Get All Receipts",
        responses = {
            @ApiResponse(responseCode = "200", description = "Receipts Fetched Successfully")
        }
    )
    public ResponseEntity<ListResponseStructure<ReceiptResponseDto>> getAllReceipts() {
        List<ReceiptResponseDto> list = receiptService.getAll();
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Receipts List",
                list
        );
    }

    // ========================
    // GET BY ID
    // ========================

    @GetMapping("/{id}")
    @Operation(
        description = "API Endpoint to Get Receipt By Id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Receipt Fetched Successfully")
        }
    )
    public ResponseEntity<ResponseStructure<ReceiptResponseDto>> getReceiptById(
            @PathVariable Long id
    ) {
        ReceiptResponseDto response = receiptService.getById(id);
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Receipt Details",
                response
        );
    }

    // ========================
    // DELETE
    // ========================

    @DeleteMapping("/{id}")
    @Operation(
        description = "API Endpoint to Delete Receipt",
        responses = {
            @ApiResponse(responseCode = "200", description = "Receipt Deleted Successfully")
        }
    )
    public ResponseEntity<ResponseStructure<String>> deleteReceipt(
            @PathVariable Long id
    ) {
        receiptService.delete(id);
        return ResponseBuilder.success(
                HttpStatus.OK,
                "Receipt Deleted Successfully",
                "Deleted"
        );
    }
}
