package com.erp.Controller.AccountDashboardView;

import com.erp.Dto.InvoiceDTO;
import com.erp.Service.AccountDashBoard.IInvoiceService;
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
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoice Controller", description = "APIs for Invoices")
@AllArgsConstructor
public class InvoiceController {

    @Autowired
    private IInvoiceService invoiceService;

    @GetMapping("/invoice-by-id/{invoiceId}")
    @Operation(description = "Fetch Invoice by ID",
            responses = {@ApiResponse(responseCode = "200", description = "Invoice retrieved")})
    public ResponseEntity<ResponseStructure<InvoiceDTO>> fetchById(@PathVariable Integer invoiceId) {
        InvoiceDTO response = invoiceService.getById(invoiceId);
        return ResponseBuilder.success(HttpStatus.OK, "Invoice retrieved successfully", response);
    }

    @PostMapping("/create-invoice")
    @Operation(description = "Create Invoice",
            responses = {@ApiResponse(responseCode = "201", description = "Invoice created")})
    public ResponseEntity<ResponseStructure<InvoiceDTO>> create(@Valid @RequestBody InvoiceDTO dto) {
        InvoiceDTO response = invoiceService.create(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Invoice created successfully", response);
    }

    @PutMapping("/update-invoice")
    @Operation(description = "Update Invoice",
            responses = {@ApiResponse(responseCode = "200", description = "Invoice updated")})
    public ResponseEntity<ResponseStructure<InvoiceDTO>> update(
                                                                @Valid @RequestBody InvoiceDTO dto) {
        InvoiceDTO response = invoiceService.update(dto);
        return ResponseBuilder.success(HttpStatus.OK, "Invoice updated successfully", response);
    }

    @DeleteMapping("/delete-invoice/{invoiceId}")
    @Operation(description = "Delete Invoice",
            responses = {@ApiResponse(responseCode = "204", description = "Invoice deleted")})
    public ResponseEntity<ResponseStructure<Void>> delete(@PathVariable Integer invoiceId) {
        invoiceService.delete(invoiceId);
        return (ResponseEntity<ResponseStructure<Void>>) (ResponseEntity<?>) ResponseBuilder.success(
                HttpStatus.NO_CONTENT,
                "Invoice deleted successfully",
                null
        );
    }

}