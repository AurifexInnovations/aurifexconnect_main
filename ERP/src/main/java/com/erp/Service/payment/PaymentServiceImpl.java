package com.erp.Service.payment;


import com.erp.Dto.Request.PaymentRequestDto;
import com.erp.Dto.Response.PaymentResponseDto;
import com.erp.Mapper.payments.PaymentMapper;
import com.erp.Model.Payment;
import com.erp.Repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto requestDto) {
        log.info("Creating payment for invoiceId={}", requestDto.getInvoiceId());

       Payment payment = PaymentMapper.toEntity(requestDto);

      Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment created successfully with id={}", savedPayment.getId());
        return PaymentMapper.toDto(savedPayment);
    }

    @Override
    public PaymentResponseDto getPaymentById(Long id) {
        log.info("Fetching payment with id={}", id);

       Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return PaymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentResponseDto> getAllPayments() {
        log.info("Fetching all payments");

        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDto> getPaymentsByInvoice(Long invoiceId) {
        log.info("Fetching payments for invoiceId={}", invoiceId);

        return paymentRepository.findByInvoiceId(invoiceId)
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePayment(Long id) {
        log.info("Deleting payment with id={}", id);
        paymentRepository.deleteById(id);
    }
}
