package com.erp.Controller.Payment;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaymentRequestDTO;
import com.erp.Dto.Request.PaymentResponseDTO;
import com.erp.Dto.Request.UpdatePaymentRequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.Payment.PaymentService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ------------------------ CREATE ------------------------
    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<PaymentResponseDTO>> createPayment(
            @RequestBody @Valid PaymentRequestDTO dto) {

        log.info("START :: createPayment() with request: {}", dto);

        PaymentResponseDTO response = paymentService.createPayment(dto);

        log.info("END :: createPayment() => Payment created with ID: {}", response.getPaymentId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.<PaymentResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Payment created successfully")
                        .data(response)
                        .build());
    }

    // ------------------------ FILTER / LIST ------------------------
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<PaymentResponseDTO>>> getPayments(
            @RequestBody FilterRequest filterRequest) {

        log.info("START :: getPayments() with filterRequest: {}", filterRequest);

        ResultDto<PaymentResponseDTO> paymentResponses =
                paymentService.getFilteredPayments(filterRequest);

        log.info("END :: getPayments() => totalRecords: {}", paymentResponses.getCount());

        return ResponseBuilder.success(HttpStatus.OK,
                "Payments retrieved successfully!",
                paymentResponses);
    }

    // ------------------------ GET DETAIL ------------------------
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseStructure<PaymentResponseDTO>> getPayment(@PathVariable Long id) {

        log.info("START :: getPayment() for ID: {}", id);

        PaymentResponseDTO response = paymentService.getPaymentById(id);

        log.info("END :: getPayment() => Payment ID: {}", response.getPaymentId());

        return ResponseEntity.ok(
                ResponseStructure.<PaymentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payment details fetched successfully")
                        .data(response)
                        .build()
        );
    }

    // ------------------------ UPDATE ------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseStructure<PaymentResponseDTO>> updatePayment(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePaymentRequestDTO dto) {

        log.info("START :: updatePayment() for ID: {}, request: {}", id, dto);

        PaymentResponseDTO response = paymentService.updatePayment(id, dto);

        log.info("END :: updatePayment() => Updated Payment ID: {}", id);

        return ResponseEntity.ok(
                ResponseStructure.<PaymentResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payment updated successfully")
                        .data(response)
                        .build()
        );
    }

    // ------------------------ DEACTIVATE ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deletePayment(@PathVariable Long id) {

        log.info("START :: deletePayment() for ID: {}", id);

        paymentService.deactivatePayment(id);

        log.info("END :: deletePayment() => Payment marked inactive for ID: {}", id);

        return ResponseEntity.ok(
                ResponseStructure.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payment deactivated successfully")
                        .data("Payment has been marked inactive")
                        .build()
        );
    }
}
