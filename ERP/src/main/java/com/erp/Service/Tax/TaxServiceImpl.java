package com.erp.Service.Tax;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.TaxAnalyticsRequest;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Mapper.Tax.TaxMapper;
import com.erp.Model.Tax;
import com.erp.Repository.Tax.TaxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;
    private final TaxMapper taxMapper;

    @Override
    public TaxResponse addTax(TaxRequest taxRequest) {
        Tax tax = taxMapper.mapToTax(taxRequest);
        taxRepository.save(tax);
        return taxMapper.mapToTaxResponse(tax);
    }

    @Override
    public TaxResponse updateTax(TaxRequest taxRequest) {
        Tax existingTax = taxRepository.findById(taxRequest.getId())
                .orElseThrow(() -> new TaxNotFoundException("Tax not found with Id: " + taxRequest.getId()));

        taxMapper.mapToTaxEntity(taxRequest, existingTax);
        taxRepository.save(existingTax);
        return taxMapper.mapToTaxResponse(existingTax);
    }

    @Override
    public TaxResponse getTaxById(CommanParam param) {
        Tax tax = taxRepository.findById(param.getId())
                .orElseThrow(() -> new TaxNotFoundException("Tax not found with Id: " + param.getId()));
        return taxMapper.mapToTaxResponse(tax);
    }

    @Override
    public List<TaxResponse> getAllTaxes() {
        List<Tax> taxes = taxRepository.findAll();
        return taxMapper.mapToTaxResponse(taxes);
    }

    @Override
    public TaxResponse deleteTax(CommanParam param) {
        Tax tax = taxRepository.findById(param.getId())
                .orElseThrow(() -> new TaxNotFoundException("Tax not found with Id: " + param.getId()));
        taxRepository.deleteById(param.getId());
        return taxMapper.mapToTaxResponse(tax);
    }


    @Override
    public List<Map<String, Object>> getTotalTaxAnalytics(TaxAnalyticsRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (request.getStartDate() == null || request.getEndDate() == null) {
            return result;
        }

        LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = request.getEndDate().atTime(23, 59, 59);

        List<Tax> taxes = taxRepository.findByCreatedAtBetween(startDateTime, endDateTime);

        for (Tax tax : taxes) {
            LocalDate date = tax.getCreatedAt().toLocalDate();
            double amount = tax.getTaxRate().doubleValue(); // using taxRate as the amount

            boolean found = false;

            for (Map<String, Object> entry : result) {
                if (entry.get("date").equals(date)) {
                    double currentTotal = (double) entry.get("totalTax");
                    entry.put("totalTax", currentTotal + amount);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Map<String, Object> newEntry = new HashMap<>();
                newEntry.put("date", date);
                newEntry.put("totalTax", amount);
                result.add(newEntry);
            }
        }

        return result;
    }


    @Override
    public Map<String, Double> getTaxBreakupAnalytics(TaxAnalyticsRequest request) {
        Map<String, Double> result = new HashMap<>();

        if (request.getStartDate() == null || request.getEndDate() == null) {
            return result;
        }

        LocalDateTime startDateTime = request.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = request.getEndDate().atTime(23, 59, 59);

        List<Tax> taxes = taxRepository.findByCreatedAtBetween(startDateTime, endDateTime);

        for (Tax tax : taxes) {
            String type = tax.getTaxType().name();
            double amount = tax.getTaxRate().doubleValue(); // using taxRate as the amount

            result.put(type, result.getOrDefault(type, 0.0) + amount);
        }

        return result;
    }
}
