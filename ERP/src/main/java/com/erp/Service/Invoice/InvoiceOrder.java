package com.erp.Service.Invoice;

import com.erp.Dto.Request.InvoiceRequestDto;
import com.erp.Dto.Response.InvoiceResponseDto;
import com.erp.Model.Invoice;
import com.erp.Projection.InvoiceProjection;

import java.util.List;

public interface InvoiceOrder {


    InvoiceResponseDto addInvoice(InvoiceRequestDto request);

    Invoice addOrUpdateInvoice(InvoiceRequestDto request);

    InvoiceResponseDto getInvoiceById(Long id);

    List<InvoiceProjection> getAllInvoices(Long invoiceId, int page, int size);

    void deleteInvoice(Long id);

}
