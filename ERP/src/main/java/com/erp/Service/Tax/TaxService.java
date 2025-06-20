package com.erp.Service.Tax;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaxService {
    TaxResponse addTax(TaxRequest taxRequest);
    TaxResponse updateTax(TaxRequest taxRequest);
    TaxResponse getTaxById(CommanParam param);
    List<TaxResponse> getAllTaxes();
    TaxResponse deleteTax(CommanParam param);
    List<Map<String, Object>> getTotalTaxAnalytics(LocalDate startDate, LocalDate endDate);
    Map<String, Double> getTaxBreakupAnalytics(LocalDate startDate, LocalDate endDate);
}
