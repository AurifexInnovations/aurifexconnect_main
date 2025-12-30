package com.erp.Events.Invoice.EnhanceQuotation;

import com.erp.Model.EnhanceQuotation;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class QuotationAcceptedEvent extends ApplicationEvent {

    private final EnhanceQuotation quotation;


    public QuotationAcceptedEvent(Object source, EnhanceQuotation quotation) {
        super(source);
        this.quotation = quotation;
    }

    public EnhanceQuotation getQuotation(){
        return quotation;
    }

}
