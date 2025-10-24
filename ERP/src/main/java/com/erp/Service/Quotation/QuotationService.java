package com.erp.Service.Quotation;

import com.erp.Model.Quotation;
import java.util.List;

public interface QuotationService {

    Quotation createQuotation(Quotation quotation);

    List<Quotation> getAllQuotations();

    Quotation getQuotationById(String quotationId);

    Quotation updateQuotation(String quotationId, Quotation quotation);

    void deleteQuotation(String quotationId);
}
