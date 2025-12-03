package com.erp.Mapper.Quotation;

import com.erp.Dto.Request.QuotationRequest;
import com.erp.Dto.Response.QuotationResponse;
import com.erp.Model.Quotation;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface QuotationMapper {

    Quotation toEntity(QuotationRequest quotationRequest);

    QuotationResponse toResponse(Quotation quotation);

    List<QuotationResponse> toListResponse(List<Quotation> quotations);

}
