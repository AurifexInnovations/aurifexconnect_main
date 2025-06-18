package com.erp.Service.Tax;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;

import java.util.List;

public interface TaxService {

    TaxResponse addTax(TaxRequest taxRequest);

    TaxResponse updateTax(TaxRequest taxRequest);

    TaxResponse getTaxById(CommanParam param);

    List<TaxResponse> getAllTaxes();

    TaxResponse deleteTax(CommanParam param);
}
