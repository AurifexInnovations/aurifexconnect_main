package com.erp.Controller.Invoice;

import com.erp.Dto.Request.InvoiceRequestDto;
import com.erp.Dto.Response.InvoiceResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Invoice;
import com.erp.Projection.InvoiceProjection;
import com.erp.Service.Invoice.InvoiceOrder;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceOrderController {

    private final InvoiceOrder invoiceService;

    // ✅ ADD
    @PostMapping("/add")
    public ResponseEntity<ResponseStructure<InvoiceResponseDto>> addInvoice(
            @RequestBody InvoiceRequestDto request) {

        log.info("API /api/invoices/add called");

        InvoiceResponseDto response = invoiceService.addInvoice(request);

        return ResponseBuilder.success(
                HttpStatus.CREATED,
                "Invoice Created Successfully",
                response
        );
    }

    // ✅ ADD OR UPDATE
    @PostMapping("/update")
    public ResponseEntity<ResponseStructure<Invoice>> addOrUpdateInvoice(
            @RequestBody InvoiceRequestDto request) {

        log.info("API /api/invoices/addOrUpdate called");

        Invoice invoice = invoiceService.addOrUpdateInvoice(request);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Invoice Saved Successfully",
                invoice
        );
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<InvoiceResponseDto>> getInvoiceById(
            @PathVariable Long id) {

        log.info("API /api/invoices/{} called", id);

        InvoiceResponseDto response = invoiceService.getInvoiceById(id);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Invoice Found",
                response
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseStructure< ResultDto<InvoiceResponseDto>>> getAllInvoices(
    ) {

        log.info("API /api/invoices/all called");

        ResultDto<InvoiceResponseDto> list = invoiceService.getAllInvoices();

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Invoices List",
                list
        );
    }


    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteInvoice(
            @PathVariable Long id) {

        log.info("API /api/invoices/delete/{} called", id);

        invoiceService.deleteInvoice(id);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Invoice Deleted Successfully",
                "DELETED"
        );
    }
}
