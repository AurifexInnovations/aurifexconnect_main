package com.erp.Service.Quotation;

import com.erp.Dto.Request.QuotationRequest;
import com.erp.Dto.Response.QuotationResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.Quotation.QuotationNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Quotation.QuotationMapper;
import com.erp.Model.Quotation;
import com.erp.Repository.Quotation.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuotationServiceImpl implements QuotationService {

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private QuotationMapper quotationMapper;

    @Override
    public QuotationResponse createQuotation(QuotationRequest quotation) {

        Quotation saved = quotationMapper.toEntity(quotation);
        return quotationMapper.toResponse(quotationRepository.save(saved));
    }

    @Override
    public ResultDto<QuotationResponse> getAllQuotations() {

        List<QuotationResponse> list = quotationMapper.toListResponse(quotationRepository.findAll());
        ResultDto<QuotationResponse> resultDto = new ResultDto<>();

        resultDto.setResults(list != null ? list : List.of());
        resultDto.setCount(list != null ? list.size() : 0);

        return resultDto;
    }

    @Override
    public QuotationResponse getQuotationById(String quotationId) {
        Quotation quotation = quotationRepository.findByQuotationId(quotationId);

        if (quotation == null) throw new QuotationNotFoundException("Quotation Not Found With Id : " + quotationId);

        return quotationMapper.toResponse(quotation);
    }

    @Override
    public QuotationResponse updateQuotation(QuotationRequest quotation) {
        Quotation existing = quotationRepository.findByQuotationId(quotation.getQuotationId());
        if (existing == null)
            throw new QuotationNotFoundException("Quotation Not Found With Id : " + quotation.getQuotationId());

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

        Quotation updated = quotationRepository.save(existing);

        return quotationMapper.toResponse(updated);
    }

    @Override
    public void deleteQuotation(String quotationId) {
        Quotation existing = quotationRepository.findByQuotationId(quotationId);

        if (existing == null) throw new QuotationNotFoundException("Quotation Not Found With Id : " + quotationId);

        quotationRepository.delete(existing);
    }
}
