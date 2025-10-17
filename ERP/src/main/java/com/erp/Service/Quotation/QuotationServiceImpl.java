package com.erp.Service.Quotation;

import com.erp.Model.Quotation;
import com.erp.Repository.Quotation.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationServiceImpl implements QuotationService {

    @Autowired
    private QuotationRepository quotationRepository;

    @Override
    public Quotation createQuotation(Quotation quotation) {
        return quotationRepository.save(quotation);
    }

    @Override
    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }

    @Override
    public Quotation getQuotationById(String quotationId) {
        return quotationRepository.findByQuotationId(quotationId);
    }

    @Override
    public Quotation updateQuotation(String quotationId, Quotation quotation) {
        Quotation existing = quotationRepository.findByQuotationId(quotationId);
        if (existing == null) return null;

        existing.setType(quotation.getType());
        existing.setCustomerId(quotation.getCustomerId());
        existing.setAddress(quotation.getAddress());
        existing.setContactPerson(quotation.getContactPerson());
        existing.setQuotationDate(quotation.getQuotationDate());
        existing.setValidityDate(quotation.getValidityDate());
        existing.setPaymentTerms(quotation.getPaymentTerms());
        existing.setTotalAmount(quotation.getTotalAmount());
        existing.setStatus(quotation.getStatus());
        existing.setNotes(quotation.getNotes());
        existing.setLanguage(quotation.getLanguage());

        return quotationRepository.save(existing);
    }

    @Override
    public void deleteQuotation(String quotationId) {
        Quotation existing = quotationRepository.findByQuotationId(quotationId);
        if (existing != null) {
            quotationRepository.delete(existing);
        }
    }
}
