package com.erp.Service.Tax;

import com.erp.CustomRepository.TaxCustomRepository;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Mapper.Tax.TaxMapper;
import com.erp.Model.Tax;
import com.erp.Projection.TaxProjection;
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
    private final TaxCustomRepository taxCustomRepository;

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
        return taxMapper.mapToTaxResponse(taxRepository.findAll());
    }

    @Override
    public TaxResponse deleteTax(CommanParam param)
    {
        Tax tax = taxRepository.findById(param.getId())
                .orElseThrow(() -> new TaxNotFoundException("Tax not found with Id: " + param.getId()));
        taxRepository.deleteById(param.getId());
        return taxMapper.mapToTaxResponse(tax);
    }

    @Override
    public List<Map<String, Object>> getTotalTaxAnalytics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay(); // include full end day

        List<Tax> taxes = taxRepository.findByCreatedAtBetween(start, end);

        Map<LocalDate, Double> taxByDate = new TreeMap<>();
        for (Tax tax : taxes) {
            if (tax.getCreatedAt() != null && tax.getTaxRate() != null) {
                LocalDate date = tax.getCreatedAt().toLocalDate();
                taxByDate.merge(date, tax.getTaxRate().doubleValue(), Double::sum);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<LocalDate, Double> entry : taxByDate.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", entry.getKey());
            map.put("totalTax", entry.getValue());
            result.add(map);
        }

        return result;
    }

    @Override
    public Map<String, Double> getTaxBreakupAnalytics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay(); // include full end day

        List<Tax> taxes = taxRepository.findByCreatedAtBetween(start, end);

        Map<String, Double> result = new HashMap<>();
        for (Tax tax : taxes) {
            if (tax.getTaxName() != null && tax.getTaxRate() != null) {
                String name = tax.getTaxName().name();
                result.put(name, result.getOrDefault(name, 0.0) + tax.getTaxRate().doubleValue());
            }
        }

        return result;
    }


    @Override
    public List<TaxProjection> findTaxesByFilter(FilterRequest filterRequest) {
        if (filterRequest == null) {
            throw new TaxNotFoundException("Filter data is required!");
        }

        List<TaxProjection> projections = taxCustomRepository.getTaxDetails(filterRequest);

        if (projections == null || projections.isEmpty()) {
            throw new TaxNotFoundException("No taxes found for the given filter!");
        }

        return projections;
    }

}

