package com.erp.Service.AccountDashBoard;

import com.erp.Dto.ReceiptDTO;
import com.erp.Mapper.AccountDashBoard.ReceiptMapper;
import com.erp.Model.Receipt;
import com.erp.Repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements IReceiptService {

    @Autowired
    private ReceiptRepository repository;

    ReceiptMapper rm = new ReceiptMapper();
    
    @Override
    public ReceiptDTO getById(Integer id) {
        Receipt entity = repository.findById(id).orElse(null);
        return entity != null ? rm.toDto(entity) : null;
    }

    @Override
    public ReceiptDTO create(ReceiptDTO dto) {
        Receipt entity = rm.toEntity(dto);
        return rm.toDto(repository.save(entity));
    }

    @Override
    public ReceiptDTO update(Integer id, ReceiptDTO dto) {
        Receipt entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + id));

        // Update fields from DTO
        entity.setInvoiceId(dto.getInvoiceId());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setVoucherId(dto.getVoucherId());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setNotes(dto.getNotes());

        Receipt updated = repository.save(entity);
        return rm.toDto(updated);
    }

    @Override
    public void delete(Integer id) {
        Receipt entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + id));
        repository.delete(entity);
    }

}