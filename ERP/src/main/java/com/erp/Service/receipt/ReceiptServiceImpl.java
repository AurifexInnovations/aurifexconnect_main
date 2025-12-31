package com.erp.Service.receipt;


import com.erp.Dto.Request.ReceiptRequestDto;
import com.erp.Dto.Response.ReceiptResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.receipt.ReceiptMapper;
import com.erp.Model.Branch;
import com.erp.Model.Receipt;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.receipt.ReceiptRepository;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final BranchRepository branchRepository;

    // ========================
    // CREATE
    // ========================
    @Override
    public ReceiptResponseDto create(ReceiptRequestDto requestDto) {

        log.info("Creating receipt | request={}", requestDto);

        validateRequest(requestDto);

        Receipt receipt = ReceiptMapper.toEntity(requestDto);
        if (requestDto.getBranchId() != null){
            Branch branch =  branchRepository.findById(requestDto.getBranchId())
                    .orElseThrow(()-> new ResourceNotFoundException("Branch not Found with this Branch Id : "+requestDto.getBranchId()));
            receipt.setBranch(branch);

        }
        receipt.setReceiptNumber(NumberGeneratorUtil.generate("RCT",receiptRepository.count()+1));
        Receipt savedReceipt = receiptRepository.save(receipt);

        log.info("Receipt created successfully | receiptId={}", savedReceipt.getId());

        return ReceiptMapper.toDto(savedReceipt);
    }

    // ========================
    // UPDATE
    // ========================
    @Override
    public ReceiptResponseDto update(Long id, ReceiptRequestDto requestDto) {

        log.info("Updating receipt | receiptId={} | request={}", id, requestDto);

        if (id == null || id <= 0) {
            log.error("Invalid receipt id for update | id={}", id);
            throw new IllegalArgumentException("Invalid receipt id");
        }

        validateRequest(requestDto);

        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Receipt not found for update | id={}", id);
                    return new RuntimeException("Receipt not found");
                });

        if (requestDto.getBranchId() != null){
            Branch branch =  branchRepository.findById(requestDto.getBranchId())
                    .orElseThrow(()-> new ResourceNotFoundException("Branch not Found with this Branch Id : "+requestDto.getBranchId()));
            receipt.setBranch(branch);

        }

        receipt.setPaymentId(requestDto.getPaymentId());
        receipt.setInvoiceId(requestDto.getInvoiceId());
        receipt.setCustomerId(requestDto.getCustomerId());
        receipt.setReceiptNumber(requestDto.getReceiptNumber());
        receipt.setAmountReceived(requestDto.getAmountReceived());
        receipt.setPaymentMethod(requestDto.getPaymentMethod());
        receipt.setNotes(requestDto.getNotes());

        Receipt updatedReceipt = receiptRepository.save(receipt);

        log.info("Receipt updated successfully | receiptId={}", updatedReceipt.getId());

        return ReceiptMapper.toDto(updatedReceipt);
    }

    // ========================
    // GET ALL
    // ========================
    @Override
    public List<ReceiptResponseDto> getAll() {

        log.info("Fetching all receipts");

        List<Receipt> receipts = receiptRepository.findAll();

        log.info("Total receipts fetched = {}", receipts.size());

        return receipts.stream()
                .map(ReceiptMapper::toDto)
                .collect(Collectors.toList());
    }

    // ========================
    // GET BY ID
    // ========================
    @Override
    public ReceiptResponseDto getById(Long id) {

        log.info("Fetching receipt by id | id={}", id);

        if (id == null || id <= 0) {
            log.error("Invalid receipt id | id={}", id);
            throw new IllegalArgumentException("Invalid receipt id");
        }

        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Receipt not found | id={}", id);
                    return new RuntimeException("Receipt not found");
                });

        log.info("Receipt fetched successfully | receiptId={}", receipt.getId());

        return ReceiptMapper.toDto(receipt);
    }

    // ========================
    // DELETE
    // ========================
    @Override
    public void delete(Long id) {

        log.info("Deleting receipt | id={}", id);

        if (id == null || id <= 0) {
            log.error("Invalid receipt id for delete | id={}", id);
            throw new IllegalArgumentException("Invalid receipt id");
        }

        if (!receiptRepository.existsById(id)) {
            log.error("Receipt not found for delete | id={}", id);
            throw new RuntimeException("Receipt not found");
        }

        receiptRepository.deleteById(id);

        log.info("Receipt deleted successfully | id={}", id);
    }

    @Override
    public ResultDto<ReceiptResponseDto> getAllByBranchId(Long branchId) {


        List<ReceiptResponseDto> responseDtos = new ArrayList<>();
        for (Receipt receipt : receiptRepository.findAllByBranchBranchId(branchId)){
            responseDtos.add(ReceiptMapper.toDto(receipt));
        }
        ResultDto<ReceiptResponseDto> responseDtoResultDto = new ResultDto<>();
        responseDtoResultDto.setCount(responseDtos.size());
        responseDtoResultDto.setResults(responseDtos);

        return responseDtoResultDto;
    }

    // ========================
    // VALIDATION METHOD
    // ========================
    private void validateRequest(ReceiptRequestDto requestDto) {

        if (requestDto == null) {
            log.error("Receipt request is null");
            throw new IllegalArgumentException("Receipt request cannot be null");
        }

        if (requestDto.getPaymentId() == null || requestDto.getPaymentId() <= 0) {
            throw new IllegalArgumentException("Invalid paymentId");
        }

        if (requestDto.getInvoiceId() == null || requestDto.getInvoiceId() <= 0) {
            throw new IllegalArgumentException("Invalid invoiceId");
        }

        if (requestDto.getCustomerId() == null || requestDto.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Invalid customerId");
        }

        if (requestDto.getAmountReceived() == null ||
                requestDto.getAmountReceived().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount received must be greater than zero");
        }
    }
}
