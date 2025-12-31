package com.erp.Service.payment;


import com.erp.Dto.Request.PaymentRequestDto;
import com.erp.Dto.Response.PaymentResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.PaymentStatus;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.payments.PaymentMapper;
import com.erp.Model.*;
import com.erp.Repository.Amc.AmcRepository;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Invoice.InvoiceMasterRepository;
import com.erp.Repository.payment.PaymentRepository;
import com.erp.Repository.receipt.ReceiptRepository;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReceiptRepository receiptRepository;
    private final InvoiceMasterRepository invoiceRepository;
    private final BranchRepository branchRepository;
    private final AmcRepository amcRepository;

    @Override
    public PaymentResponseDto updatePayment(Long id, PaymentRequestDto requestDto) {

        log.info("Updating payment id={}", id);

        //  Fetch existing payment
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id));

        // 2 Update fields using mapper (NO new object created)
        PaymentMapper.updateEntity(requestDto, payment);

        // 3️ Update branch if provided
        if (requestDto.getBranchId() != null) {
            Branch branch = branchRepository.findById(requestDto.getBranchId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Branch not Found with id: " + requestDto.getBranchId()));
            payment.setBranch(branch);
        }

        // 4️ Save → UPDATE happens
        Payment savedPayment = paymentRepository.save(payment);

        // 5️ Business logic
        if (savedPayment.getPaymentStatus() == PaymentStatus.PAID) {

            Invoice invoice = invoiceRepository.findById(savedPayment.getInvoiceId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Invoice Not Found With id: " + savedPayment.getInvoiceId()));

            invoice.setPaymentStatus("PAID");
            invoiceRepository.save(invoice);

        // Amc Amount update based on Payment done
            updateAmcAmounts(invoice);

        // add Receipt base on Payment Done
            addReceipt(savedPayment);
        }

        log.info("Payment updated successfully with id={}", savedPayment.getId());

        return PaymentMapper.toDto(savedPayment);
    }

    private void updateAmcAmounts(Invoice invoice) {
        Amc amc = amcRepository.findBySalesOrderSalesOrderNumber(invoice.getSalesOrderId())
                        .orElseThrow(()-> new ResourceNotFoundException("Amc not found with this SalesOrder id:"+ invoice.getSalesOrderId()));

        BigDecimal perCycleAmount = amc.getPerCycleAmount();
        amc.setTotalCompleteAmount(
                amc.getTotalCompleteAmount().add(perCycleAmount)
        );

        amc.setTotalRemainAmount(
                amc.getTotalRemainAmount().subtract(perCycleAmount)
        );

        amcRepository.save(amc);
    }


    private void addReceipt(Payment payment) {
        Receipt receipt = new Receipt();
        receipt.setReceiptNumber(NumberGeneratorUtil.generate("RCT",receiptRepository.count()+1));
        receipt.setPaymentId(payment.getId());
        receipt.setInvoiceId(payment.getInvoiceId());
        receipt.setCustomerId(payment.getCustomerId());
        receipt.setReceiptDate(LocalDateTime.now());
        receipt.setBranch(payment.getBranch());
        receipt.setAmountReceived(payment.getAmountPaid());
        receipt.setPaymentMethod(payment.getPaymentMethod());
        receiptRepository.save(receipt);
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

    @Override
    public ResultDto<PaymentResponseDto> getAllByBranchId(Long branchId) {


        List<PaymentResponseDto> responseDtos = new ArrayList<>();
        for (Payment payment : paymentRepository.findAllByBranchBranchId(branchId)){
            responseDtos.add(PaymentMapper.toDto(payment));
        }
        ResultDto<PaymentResponseDto> responseDtoResultDto = new ResultDto<>();
        responseDtoResultDto.setCount(responseDtos.size());
        responseDtoResultDto.setResults(responseDtos);

        return responseDtoResultDto;
    }
}
