package com.erp.Service.Payment;

import com.erp.Dto.Request.PaymentRequestDTO;
import com.erp.Dto.Request.PaymentResponseDTO;
import com.erp.Dto.Request.UpdatePaymentRequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Request.FilterRequest;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentRequestDTO dto);

    ResultDto<PaymentResponseDTO> getFilteredPayments(FilterRequest filterRequest);

    PaymentResponseDTO getPaymentById(Long id);

    PaymentResponseDTO updatePayment(Long id, UpdatePaymentRequestDTO dto);

    void deactivatePayment(Long id);
}
