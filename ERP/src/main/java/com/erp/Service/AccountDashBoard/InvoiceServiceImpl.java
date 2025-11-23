package com.erp.Service.AccountDashBoard;

import com.erp.Dto.InvoiceDTO;
import com.erp.Mapper.AccountDashBoard.InvoiceMapper;
import com.erp.Model.Invoice;
import com.erp.Repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements IInvoiceService {

    @Autowired
    private InvoiceRepository repository;
    
    InvoiceMapper im = new InvoiceMapper();

    @Override
    public InvoiceDTO getById(Integer id) {
        Invoice entity = repository.findById(id).orElse(null);
        return entity != null ? im.toDto(entity) : null;
    }

    @Override
    public InvoiceDTO create(InvoiceDTO dto) {
        Invoice entity = im.toEntity(dto);
        return im.toDto(repository.save(entity));
    }

    @Override
    public InvoiceDTO update(InvoiceDTO dto) {
        Invoice entity = repository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + dto.getInvoiceId()));

        // Update fields from DTO
        entity.setSoId(dto.getSoId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setDate(dto.getDate());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setTaxId(dto.getTaxId());
        entity.setPendingStatus(dto.getPendingStatus());
        entity.setInvoiceNumber(dto.getInvoiceNumber());

        Invoice updated = repository.save(entity);
        return im.toDto(updated);
    }

    @Override
    public void delete(Integer id) {
        Invoice entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        repository.delete(entity);
    }

}