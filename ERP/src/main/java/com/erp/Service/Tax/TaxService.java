package com.erp.Service.Tax;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.TaxAnalyticsRequest;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;

import java.util.List;
import java.util.Map;

public interface TaxService {

    TaxResponse addTax(TaxRequest taxRequest);

    TaxResponse updateTax(TaxRequest taxRequest);

    TaxResponse getTaxById(CommanParam param);

    List<TaxResponse> getAllTaxes();

    TaxResponse deleteTax(CommanParam param);

    List<Map<String, Object>> getTotalTaxAnalytics(TaxAnalyticsRequest request);

    Map<String, Double> getTaxBreakupAnalytics(TaxAnalyticsRequest request);

}
