package com.erp.Service.EnhanceQuotation;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.QuotationRequestDto;
import com.erp.Dto.Response.QuotationResponseDto;
import com.erp.Dto.Response.ResultDto;

import java.util.List;

public interface EnhanceQuotationService {

    QuotationResponseDto addQuotation(QuotationRequestDto quotationRequestDto);

    QuotationResponseDto quotationGetById(CommanParam param);


    List<QuotationResponseDto> quotationGetAll();

    String updateStatus(CommanParam param);

    ResultDto<QuotationResponseDto> getAllByBranchId(Long branchId);
}
