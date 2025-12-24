package com.erp.Mapper.payments;


import com.erp.Dto.Request.PaymentRequestDto;
import com.erp.Dto.Response.PaymentResponseDto;
import com.erp.Model.Payment;


public class PaymentMapper {

    public static Payment toEntity(PaymentRequestDto dto) {
        Payment payment = new Payment();
        payment.setInvoiceId(dto.getInvoiceId());
        payment.setCustomerId(dto.getCustomerId());
        payment.setInvoiceAmount(dto.getInvoiceAmount());
        payment.setAmountPaid(dto.getAmountPaid());

        payment.setTotalPaidTillNow(dto.getTotalPaidTillNow());
        payment.setBalanceAmount(dto.getBalanceAmount());

        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionReference(dto.getTransactionReference());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setNotes(dto.getNotes());

        return payment;
    }

    public static PaymentResponseDto toDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setInvoiceId(payment.getInvoiceId());
        dto.setCustomerId(payment.getCustomerId());
        dto.setBranchId(payment.getBranch().getBranchId());
        dto.setInvoiceAmount(payment.getInvoiceAmount());
        dto.setAmountPaid(payment.getAmountPaid());
        dto.setTotalPaidTillNow(payment.getTotalPaidTillNow());
        dto.setBalanceAmount(payment.getBalanceAmount());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setTransactionReference(payment.getTransactionReference());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setNotes(payment.getNotes());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}
