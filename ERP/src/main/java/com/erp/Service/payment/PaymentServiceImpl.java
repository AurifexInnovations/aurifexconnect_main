package com.erp.Service.payment;


import com.erp.Dto.Request.PaymentRequestDto;
import com.erp.Dto.Response.PaymentResponseDto;
import com.erp.Enum.InvoiceStatus;
import com.erp.Enum.PaymentStatus;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.payments.PaymentMapper;
import com.erp.Model.Branch;
import com.erp.Model.Invoice;
import com.erp.Model.Payment;
import com.erp.Model.Receipt;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Invoice.InvoiceMasterRepository;
import com.erp.Repository.payment.PaymentRepository;
import com.erp.Repository.receipt.ReceiptRepository;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public PaymentResponseDto updatePayment(PaymentRequestDto requestDto) {
        log.info("Creating payment for invoiceId={}", requestDto.getInvoiceId());

       Payment payment = PaymentMapper.toEntity(requestDto);

        if (requestDto.getBranchId() != null){
            Branch branch =  branchRepository.findById(requestDto.getBranchId())
                    .orElseThrow(()-> new ResourceNotFoundException("Branch not Found with this Branch Id : "+requestDto.getBranchId()));
            payment.setBranch(branch);

        }

      Payment savedPayment = paymentRepository.save(payment);


      if (savedPayment.getPaymentStatus().equals(PaymentStatus.PAID)){
          Invoice invoice = invoiceRepository.findById(payment.getInvoiceId())
                          .orElseThrow(()-> new ResourceNotFoundException("Invoice Not Found With this invoice Id : " + payment.getInvoiceId()));

          invoice.setPaymentStatus("PAID");
          invoiceRepository.save(invoice);

          addReceipt(payment);
      }

        log.info("Payment created successfully with id={}", savedPayment.getId());
        return PaymentMapper.toDto(savedPayment);
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
}
