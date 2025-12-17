package com.erp.Service.EnhanceQuotation;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.QuotationRequestDto;
import com.erp.Dto.Response.QuotationResponseDto;

import java.util.List;

public interface EnhanceQuotationService {

    QuotationResponseDto addQuotation(QuotationRequestDto quotationRequestDto);

    QuotationResponseDto quotationGetById(CommanParam param);


    List<QuotationResponseDto> quotationGetAll();
}
