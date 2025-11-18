package com.erp.Service.Quotation;

import com.erp.Dto.Request.QuotationRequest;
import com.erp.Dto.Response.QuotationResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Quotation;
import java.util.List;

public interface QuotationService {

    QuotationResponse createQuotation(QuotationRequest quotation);

    ResultDto<QuotationResponse> getAllQuotations();

    QuotationResponse getQuotationById(String quotationId);

    QuotationResponse updateQuotation(QuotationRequest quotation);

    void deleteQuotation(String quotationId);
}
