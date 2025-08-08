package com.erp.Mapper.Tax;

import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;
import com.erp.Model.Tax;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface TaxMapper {

    Tax mapToTax(TaxRequest taxRequest);

    void mapToTaxEntity(TaxRequest taxRequest, @MappingTarget Tax tax);

    TaxResponse mapToTaxResponse(Tax tax);

    List<TaxResponse> mapToTaxResponse(List<Tax> taxList);
}
