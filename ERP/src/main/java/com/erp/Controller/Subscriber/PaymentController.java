package com.erp.Controller.Subscriber;

import com.erp.Dto.SubscriptionsDto.PaymentDto;
import com.erp.Service.SubscriptionService.IPaymentService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment Controller", description = "APIs for Payment Details")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/id/{paymentId}")
    @Operation(description = "Fetch payment details by payment ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment details retrieved")
            })
    public ResponseEntity<ResponseStructure<PaymentDto>> fetchPaymentDetails(@PathVariable Long paymentId) {
        PaymentDto response = paymentService.fetchPaymentById(paymentId);
        return ResponseBuilder.success(HttpStatus.OK, "Retrieved Payment Details.", response);
    }
}