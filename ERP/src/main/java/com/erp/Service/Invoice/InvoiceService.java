package com.erp.Service.Invoice;

import com.erp.Dto.Request.InvoiceRequest;
import com.erp.Model.InvoiceGenerator;

public interface InvoiceService {

    InvoiceGenerator createInvoice(InvoiceRequest request);

    InvoiceGenerator fetchInvoice(InvoiceRequest request);


}
