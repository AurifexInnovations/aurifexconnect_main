package com.erp.Service.Invoice;

import com.erp.Dto.Request.InvoiceRequest;
import com.erp.Exception.Master.MasterNotFoundException;
import com.erp.Model.Admin;
import com.erp.Model.InvoiceGenerator;
import com.erp.Model.Master;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Invoice.InvoiceRepository;
import com.erp.Repository.Master.MasterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService{

    private final MasterRepository masterRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public InvoiceGenerator createInvoice(InvoiceRequest request) {


        Master master = masterRepository.findById(request.getMasterId())
                .orElseThrow(()-> new MasterNotFoundException("Master with" + request.getMasterId() + " that id Not Found"));

        InvoiceGenerator invoice = new InvoiceGenerator();
        invoice.setMaster(master);

        return invoiceRepository.save(invoice);
    }


    @Override
    public InvoiceGenerator fetchInvoice(InvoiceRequest request){

        return invoiceRepository.findById(request.getMasterId())
                .orElseThrow(()-> new MasterNotFoundException("Invoice Not Found"));

        //invoice.setMaster(invoice.getMaster());
    }


}

