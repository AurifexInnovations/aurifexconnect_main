package com.erp.Mapper.Master;

import com.erp.Dto.Request.MasterRequest;
import com.erp.Dto.Response.MasterResponse;
import com.erp.Enum.TaxName;
import com.erp.Model.LineItemTax;
import com.erp.Model.LineItems;
import com.erp.Model.Master;
import com.erp.Model.Tax;
import com.erp.Repository.Tax.TaxRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@AllArgsConstructor
public class MasterMapperImpl implements MasterMapper {

    private final TaxRepository taxRepository;

    @Override
    public Master mapToMasterRequest(MasterRequest masterRequest) {

        if (masterRequest == null) {
            return null;
        }

        Master master = new Master();
        master.setAmount(masterRequest.getAmount());
        master.setName(masterRequest.getName());
        master.setDescription(masterRequest.getDescription());
        master.setVoucherType(masterRequest.getVoucherType());
        master.setVoucherIndex(masterRequest.getVoucherIndex());

        return master;
    }

    @Override
    public void mapToMasterEntity(MasterRequest masterRequest, Master master) {

        master.setAmount(masterRequest.getAmount());
        master.setName(masterRequest.getName());
        master.setDescription(masterRequest.getDescription());
        master.setVoucherType(masterRequest.getVoucherType());
        master.setVoucherIndex(masterRequest.getVoucherIndex());

    }

    @Override
    public MasterResponse mapToMasterResponse(Master master) {
        if (master == null) return null;

        MasterResponse response = new MasterResponse();
        response.setMasterId(master.getMasterId());
        response.setName(master.getName());
        response.setVoucherType(master.getVoucherType());
        response.setAmount(master.getAmount());
        response.setReferenceType(master.getReferenceType());
        response.setTransactionStatus(master.getTransactionStatus());
        response.setDescription(master.getDescription());
        response.setVoucherIndex(master.getVoucherIndex());
        response.setCreatedDate(master.getCreatedDate());
        response.setCreatedBy(master.getCreatedBy());
        response.setModifiedBy(master.getModifiedBy());
        response.setModifiedDate(master.getModifiedDate());

        // Handle nulls safely
        List<LineItems> lineItems = master.getLineItems();
        Map<TaxName, Double> taxBreakdown = new HashMap<>();
        List<TaxName> taxNames = new ArrayList<>();
        double taxTotal = 0.0;

        if (lineItems != null && !lineItems.isEmpty()) {
            for (LineItems item : lineItems) {
                if (item.getLineItemTaxes() != null) {
                    for (LineItemTax tax : item.getLineItemTaxes()) {
                        TaxName taxName = tax.getTax().getTaxName();
                        taxNames.add(taxName);
                        taxBreakdown.merge(taxName, tax.getTaxAmount(), Double::sum);
                        taxTotal += tax.getTaxAmount();
                    }
                }
            }
        }

        response.setApplicableTaxNames(taxNames);
        response.setTaxBreakdown(taxBreakdown);
        response.setTaxTotal(taxTotal);
        response.setTotalAmount(master.getTotalAmount());
        return response;

    }
}
