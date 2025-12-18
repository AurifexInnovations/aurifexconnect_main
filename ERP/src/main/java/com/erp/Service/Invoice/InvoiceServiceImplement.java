package com.erp.Service.Invoice;


import com.erp.Dto.Request.InvoiceRequestDto;
import com.erp.Dto.Response.InvoiceResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.invoice.InvoiceMapper;
import com.erp.Model.CustomerDetails;
import com.erp.Model.Invoice;

import com.erp.Projection.InvoiceProjection;
import com.erp.Repository.Invoice.InvoiceMasterRepository;
import com.erp.Repository.Invoice.InvoiceRepository;
import com.erp.Repository.costumer.CustomerDetailsRepository;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImplement implements InvoiceOrder {

    private final InvoiceMasterRepository invoiceRepository;
    private final CustomerDetailsRepository customerDetailsRepository;

    @Override
    public InvoiceResponseDto addInvoice(InvoiceRequestDto request) {

        log.info("Service addInvoice called");

        Invoice invoice = InvoiceMapper.toEntity(request);
        invoice.setInvoiceNumber(NumberGeneratorUtil.generate("INV",invoiceRepository.count()+1));
        invoice.setCreatedAt(LocalDateTime.now());

        return toResponseDto(invoiceRepository.save(invoice));
    }

    private InvoiceResponseDto toResponseDto(Invoice invoice){

        InvoiceResponseDto responseDto = InvoiceMapper.toDto(invoice);
        CustomerDetails customerDetails = customerDetailsRepository.findById(invoice.getCustomerId()).
                orElseThrow(()-> new ResourceNotFoundException("Customer Not FOund !!"));
        responseDto.setCustomerName(customerDetails.getCustomerName());

        return responseDto;
    }



    @Override
    public Invoice addOrUpdateInvoice(InvoiceRequestDto request) {

        log.info("Service addOrUpdateInvoice called");

        Invoice invoice = InvoiceMapper.toEntity(request);
        invoice.setUpdatedAt(LocalDateTime.now());

        return invoiceRepository.save(invoice);
    }

    @Override
    public InvoiceResponseDto getInvoiceById(Long id) {

        log.info("Service getInvoiceById id={}", id);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return toResponseDto(invoice);
    }

    @Override
    public ResultDto<InvoiceResponseDto> getAllInvoices() {

        log.info("Service getAllInvoices called");
        List<InvoiceResponseDto> responseDtos = new ArrayList<>();
        for (Invoice invoice : invoiceRepository.findAll()){
            responseDtos.add(toResponseDto(invoice));
        }
        ResultDto<InvoiceResponseDto> responseDtoResultDto = new ResultDto<>();
        responseDtoResultDto.setCount(responseDtos.size());
        responseDtoResultDto.setResults(responseDtos);

        return responseDtoResultDto;
    }

    @Override
    public void deleteInvoice(Long id) {

        log.info("Service deleteInvoice id={}", id);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoiceRepository.delete(invoice);
    }
}
