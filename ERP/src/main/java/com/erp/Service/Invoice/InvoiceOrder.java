package com.erp.Service.Invoice;

import com.erp.Dto.Request.InvoiceRequestDto;
import com.erp.Dto.Response.InvoiceResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Amc;
import com.erp.Model.Invoice;
import com.erp.Projection.InvoiceProjection;

import java.util.List;

public interface InvoiceOrder {


    InvoiceResponseDto addInvoice(InvoiceRequestDto request);

    InvoiceResponseDto addOrUpdateInvoice(InvoiceRequestDto request);

    InvoiceResponseDto getInvoiceById(Long id);

    ResultDto<InvoiceResponseDto> getAllInvoices();

    void deleteInvoice(Long id);

    void createFromAmc(Amc amc);

    ResultDto<InvoiceResponseDto> getAllByBranchId(Long branchId);
}
