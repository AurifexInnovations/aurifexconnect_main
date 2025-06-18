package com.erp.Service.Tax;

import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Request.TaxRequest;
import com.erp.Dto.Response.TaxResponse;
import com.erp.Exception.Tax.TaxNotFoundException;
import com.erp.Mapper.Tax.TaxMapper;
import com.erp.Model.Tax;
import com.erp.Repository.Tax.TaxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
