package com.erp.Controller.AccountDashboardView;

import com.erp.Dto.ReceiptDTO;
import com.erp.Service.AccountDashBoard.IReceiptService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/receipts")
@Tag(name = "Receipt Controller", description = "APIs for Receipts")
@AllArgsConstructor
public class ReceiptController {

    @Autowired
    private IReceiptService receiptService;

    @GetMapping("/{id}")
    @Operation(description = "Fetch Receipt by ID",
            responses = {@ApiResponse(responseCode = "200", description = "Receipt retrieved")})
    public ResponseEntity<ResponseStructure<ReceiptDTO>> fetchById(@PathVariable Integer receiptId) {
        ReceiptDTO response = receiptService.getById(receiptId);
        return ResponseBuilder.success(HttpStatus.OK, "Receipt retrieved successfully", response);
    }

    @PostMapping
    @Operation(description = "Create Receipt",
            responses = {@ApiResponse(responseCode = "201", description = "Receipt created")})
    public ResponseEntity<ResponseStructure<ReceiptDTO>> create(@Valid @RequestBody ReceiptDTO dto) {
        ReceiptDTO response = receiptService.create(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Receipt created successfully", response);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update Receipt",
            responses = {@ApiResponse(responseCode = "200", description = "Receipt updated")})
    public ResponseEntity<ResponseStructure<ReceiptDTO>> update(@PathVariable Integer id,
                                                                @Valid @RequestBody ReceiptDTO dto) {
        ReceiptDTO response = receiptService.update(id, dto);
        return ResponseBuilder.success(HttpStatus.OK, "Receipt updated successfully", response);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete Receipt",
            responses = {@ApiResponse(responseCode = "204", description = "Receipt deleted")})
    public ResponseEntity<ResponseStructure<Void>> delete(@PathVariable Integer id) {
        receiptService.delete(id);
        return (ResponseEntity<ResponseStructure<Void>>) (ResponseEntity<?>) ResponseBuilder.success(
                HttpStatus.NO_CONTENT,
                "Receipt deleted successfully",
                null
        );
    }
}
