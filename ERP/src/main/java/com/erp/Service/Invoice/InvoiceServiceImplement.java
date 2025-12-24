package com.erp.Service.Invoice;


import com.erp.Dto.Request.InvoiceRequestDto;
import com.erp.Dto.Response.InvoiceResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.InvoiceStatus;
import com.erp.Enum.SalesOrderType;
import com.erp.Enum.TaskCategory;
import com.erp.Enum.TaskStatus;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.invoice.InvoiceMapper;
import com.erp.Model.*;

import com.erp.Projection.InvoiceProjection;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Invoice.InvoiceMasterRepository;
import com.erp.Repository.Invoice.InvoiceRepository;
import com.erp.Repository.costumer.CustomerDetailsRepository;
import com.erp.Repository.salesOrder.SalesOrderRepository;
import com.erp.Utility.NumberGenerator.NumberGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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



    @Override
    public Invoice addOrUpdateInvoice(InvoiceRequestDto request) {

        log.info("Service addOrUpdateInvoice called");

        Invoice invoice = InvoiceMapper.toEntity(request);
        invoice.setUpdatedAt(LocalDateTime.now());

        if (request.getBranchId() != null){
            Branch branch =  branchRepository.findById(request.getBranchId())
                    .orElseThrow(()-> new ResourceNotFoundException("Branch not Found with this Branch Id : "+request.getBranchId()));
            invoice.setBranch(branch);

        }

        SalesOrder salesOrder = salesOrderRepository.findById(request.getSalesOrderId())
                .orElseThrow(() ->
                        new RuntimeException("SalesOrder not found with id: " + request.getSalesOrderId()));

        // 2️⃣ Enter IF block ONLY for SERVICE sales order
        if (InvoiceStatus.CONFIRM.equals(invoice.getStatus())
                && SalesOrderType.SERVICE.equals(salesOrder.getSoType())) {

            addTask(invoice);
        }

        return invoiceRepository.save(invoice);
    }

    private void addTask (Invoice invoice){

        Task task = new Task();
        task.setInvoiceId(invoice.getId());
        task.setCustomerId(invoice.getCustomerId());
        task.setCreatedAt(LocalDateTime.now());
        task.setTaskCategory(TaskCategory.SERVICE);
        task.setTaskStatus(TaskStatus.PENDING);

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
