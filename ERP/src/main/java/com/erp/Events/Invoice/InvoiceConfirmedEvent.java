package com.erp.Events.Invoice;

import com.erp.Model.Invoice;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class InvoiceConfirmedEvent extends ApplicationEvent {

    private final Invoice invoice;

    public InvoiceConfirmedEvent(Object source, Invoice invoice) {
        super(source);
        this.invoice = invoice;
    }

    public Invoice getInvoice(){
        return invoice;
    }

}
