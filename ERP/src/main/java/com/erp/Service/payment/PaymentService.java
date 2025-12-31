package com.erp.Service.payment;



import com.erp.Dto.Request.PaymentRequestDto;
import com.erp.Dto.Response.PaymentResponseDto;
import com.erp.Dto.Response.ResultDto;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto updatePayment(Long id,PaymentRequestDto requestDto);

    PaymentResponseDto getPaymentById(Long id);

    List<PaymentResponseDto> getAllPayments();

    List<PaymentResponseDto> getPaymentsByInvoice(Long invoiceId);

    void deletePayment(Long id);

    ResultDto<PaymentResponseDto> getAllByBranchId(Long branchId);
}
