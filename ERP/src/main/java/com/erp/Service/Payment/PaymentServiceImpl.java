package com.erp.Service.Payment;

import com.erp.CustomRepository.PaymentCustomRepository;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaymentRequestDTO;
import com.erp.Dto.Request.PaymentResponseDTO;
import com.erp.Dto.Request.UpdatePaymentRequestDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.Payment;
import com.erp.Repository.Payment.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentCustomRepository paymentCustomRepository;

    @Override
    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        log.info("Into [PaymentServiceImpl] [createPayment]");

        try {
            Payment payment = new Payment();

            payment.setBillId(dto.getBillId());
            payment.setVendorId(dto.getVendorId());
            payment.setAmountPaid(dto.getAmountPaid());
            payment.setPaymentMethod(dto.getPaymentMethod());
            payment.setVoucherId(dto.getVoucherId());
            payment.setNotes(dto.getNotes());

            // convert datePaid String → LocalDate
            if (dto.getDatePaid() != null) {
                payment.setDatePaid(LocalDate.parse(dto.getDatePaid()));
            }

            paymentRepository.save(payment);

            log.info("Exit [PaymentServiceImpl] [createPayment]");
            return mapToResponse(payment);

        } catch (Exception ex) {
            log.error("Error [createPayment] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public ResultDto<PaymentResponseDTO> getFilteredPayments(FilterRequest filterRequest) {
        log.info("Into [PaymentServiceImpl] [getFilteredPayments]");

        try {
            ResultDto<PaymentResponseDTO> responses =
                    paymentCustomRepository.getFilteredPayments(filterRequest);

            log.info("Exit [PaymentServiceImpl] [getFilteredPayments]");
            return responses;

        } catch (Exception exception) {
            log.error("Error [getFilteredPayments] :: {} :: {}", exception.getMessage(), exception);
            throw exception;
        }
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        log.info("Into [PaymentServiceImpl] [getPaymentById] :: {}", id);

        try {
            Payment payment = paymentRepository.findActivePaymentById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Active payment not found"));

            return mapToResponse(payment);

        } catch (Exception e) {
            log.error("Error [getPaymentById] :: {} :: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public PaymentResponseDTO updatePayment(Long id, UpdatePaymentRequestDTO dto) {
        log.info("Into [PaymentServiceImpl] [updatePayment] :: {}", id);

        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

            if (dto.getNotes() != null) {
                payment.setNotes(dto.getNotes());
            }

            paymentRepository.save(payment);

            return mapToResponse(payment);

        } catch (Exception ex) {
            log.error("Error [updatePayment] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional
    public void deactivatePayment(Long id) {
        log.info("Into [PaymentServiceImpl] [deactivatePayment] :: {}", id);

        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

            payment.setIsActive(false);
            paymentRepository.save(payment);

        } catch (Exception ex) {
            log.error("Error [deactivatePayment] :: {} :: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private PaymentResponseDTO mapToResponse(Payment payment) {
        PaymentResponseDTO response = new PaymentResponseDTO();

        response.setPaymentId(payment.getPaymentId());
        response.setBillId(payment.getBillId());
        response.setVendorId(payment.getVendorId());
        response.setDatePaid(payment.getDatePaid());
        response.setAmountPaid(payment.getAmountPaid());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setVoucherId(payment.getVoucherId());
        response.setPaymentNumber(payment.getPaymentNumber());
        response.setNotes(payment.getNotes());
        //response.setCreatedAt(payment.getCreatedAt());

        return response;
    }
}

