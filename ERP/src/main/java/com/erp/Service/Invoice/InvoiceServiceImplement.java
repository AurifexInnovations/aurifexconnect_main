package com.erp.Service.Invoice;


import com.erp.Dto.Request.InvoiceRequestDto;
import com.erp.Dto.Request.TaskRequest;
import com.erp.Dto.Response.InvoiceResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TaskResponse;
import com.erp.Enum.*;
import com.erp.Events.Invoice.InvoiceConfirmedEvent;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.invoice.InvoiceMapper;
import com.erp.Model.*;

import com.erp.Projection.InvoiceProjection;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Invoice.InvoiceMasterRepository;
import com.erp.Repository.Invoice.InvoiceRepository;
import com.erp.Repository.Task.TaskRepository;
import com.erp.Repository.costumer.CustomerDetailsRepository;
import com.erp.Repository.salesOrder.SalesOrderRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.TaskService.TaskService;
import com.erp.Service.TaskService.TaskServiceImpl;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImplement implements InvoiceOrder {

    private final InvoiceMasterRepository invoiceRepository;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final BranchRepository branchRepository;
    private final UserIdentity userIdentity;
    private final TaskRepository taskRepository;
    private final TaskServiceImpl taskService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public InvoiceResponseDto addInvoice(InvoiceRequestDto request) {

        log.info("Service addInvoice called");

        Invoice invoice = InvoiceMapper.toEntity(request);
        if (request.getBranchId() != null){
            Branch branch =  branchRepository.findById(request.getBranchId())
                    .orElseThrow(()-> new ResourceNotFoundException("Branch not Found with this Branch Id : "+request.getBranchId()));
            invoice.setBranch(branch);

        }
        invoice.setServiceCategory(request.getServiceCategory());
        invoice.setInvoiceNumber(NumberGeneratorUtil.generate("INV",invoiceRepository.count()+1));
        invoice.setCreatedAt(LocalDateTime.now());

        return toResponseDto(invoiceRepository.save(invoice));
    }

    private InvoiceResponseDto toResponseDto(Invoice invoice){



        InvoiceResponseDto responseDto = InvoiceMapper.toDto(invoice);
        CustomerDetails customerDetails = customerDetailsRepository.findById(invoice.getCustomerId()).
                orElseThrow(()-> new ResourceNotFoundException("Customer Not Found !!"));
        responseDto.setCustomerName(customerDetails.getCustomerName());

        return responseDto;
    }


    @Transactional
    @Override
    public InvoiceResponseDto addOrUpdateInvoice(InvoiceRequestDto request) {

        log.info("Service addOrUpdateInvoice called for id={}", request.getId());

        // 1️ Fetch existing invoice
        Invoice invoice = invoiceRepository.findById(request.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found with id: " + request.getId()
                        )
                );
        InvoiceStatus oldStatus = invoice.getStatus();

        // 2️ Update basic fields using mapper (NO extra ifs)
        InvoiceMapper.updateEntity(invoice, request);

        // 3️ Update Branch only if provided (FK → MUST be validated)
        if (request.getBranchId() != null) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Branch not found with id: " + request.getBranchId()
                            )
                    );
            invoice.setBranch(branch);
        }

        // 4️ Fetch SalesOrder only when needed for business logic
        SalesOrder salesOrder = null;
        if (request.getSalesOrderId() != null) {
            salesOrder = salesOrderRepository.findById(request.getSalesOrderId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "SalesOrder not found with id: " + request.getSalesOrderId()
                            )
                    );
        }

        // 5 Audit
        invoice.setUpdatedAt(LocalDateTime.now());

        // 6. Save invoice
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 7. Business rule: Publish event only on transition to CONFIRM
        if (!InvoiceStatus.CONFIRM.equals(oldStatus)  // Not already CONFIRM
                && InvoiceStatus.CONFIRM.equals(savedInvoice.getStatus())
                && salesOrder != null
                && SalesOrderType.SERVICE.equals(salesOrder.getSoType())) {
            eventPublisher.publishEvent(new InvoiceConfirmedEvent(this, savedInvoice));
        }

        // 8. Return response
        return toResponseDto(savedInvoice);
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
