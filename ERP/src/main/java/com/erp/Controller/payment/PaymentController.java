package com.erp.Controller.payment;

import com.erp.Dto.Request.PaymentRequestDto;
import com.erp.Dto.Response.PaymentResponseDto;
import com.erp.Service.payment.PaymentService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<PaymentResponseDto>> updatePayment(
           @PathVariable Long id, @RequestBody PaymentRequestDto requestDto) {

        log.info("API PUT /api/payments called");

        PaymentResponseDto responseDto = paymentService.updatePayment(id,requestDto);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payment Updated successfully",
                responseDto
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<PaymentResponseDto>> getPaymentById(
            @PathVariable Long id) {

        log.info("API GET /api/payments/{} called", id);

        PaymentResponseDto responseDto = paymentService.getPaymentById(id);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payment fetched successfully",
                responseDto
        );
    }

    @GetMapping
    public ResponseEntity<ListResponseStructure<PaymentResponseDto>> getAllPayments() {

        log.info("API GET /api/payments called");

        List<PaymentResponseDto> list = paymentService.getAllPayments();

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payments list fetched successfully",
                list
        );
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<ListResponseStructure<PaymentResponseDto>> getPaymentsByInvoice(
            @PathVariable Long invoiceId) {

        log.info("API GET /api/payments/invoice/{} called", invoiceId);

        List<PaymentResponseDto> list = paymentService.getPaymentsByInvoice(invoiceId);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payments fetched by invoice",
                list
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deletePayment(
            @PathVariable Long id) {

        log.info("API DELETE /api/payments/{} called", id);

        paymentService.deletePayment(id);

        return ResponseBuilder.success(
                HttpStatus.OK,
                "Payment deleted successfully",
                "SUCCESS"
        );
    }
}
