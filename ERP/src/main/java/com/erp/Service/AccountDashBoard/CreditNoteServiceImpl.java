package com.erp.Service.AccountDashBoard;

import com.erp.Dto.CreditNoteDTO;
import com.erp.Mapper.AccountDashBoard.CreditNoteMapper;
import com.erp.Model.CreditNote;
import com.erp.Repository.CreditNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditNoteServiceImpl implements ICreditNoteService {

    @Autowired
    private CreditNoteRepository repository;

    CreditNoteMapper cn = new CreditNoteMapper();

    @Override
    public CreditNoteDTO getById(Integer id) {
        CreditNote entity = repository.findById(id).orElse(null);
        return entity != null ? cn.toDto(entity) : null;
    }

    @Override
    public CreditNoteDTO create(CreditNoteDTO dto) {
        CreditNote entity = cn.toEntity(dto);
        return cn.toDto(repository.save(entity));
    }

    @Override
    public CreditNoteDTO update(CreditNoteDTO dto) {
        CreditNote entity = repository.findById(dto.getCnId())
                .orElseThrow(() -> new RuntimeException("Credit Note not found with id: " + dto.getCnId()));

        // Update fields from DTO
        entity.setInvoiceId(dto.getInvoiceId());
        entity.setDate(dto.getDate());
        entity.setAmount(dto.getAmount());
        entity.setReason(dto.getReason());
        entity.setStatus(dto.getStatus());
        entity.setCnNumber(dto.getCnNumber());

        CreditNote updated = repository.save(entity);
        return cn.toDto(updated);
    }

    @Override
    public void delete(Integer id) {
        CreditNote entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit Note not found with id: " + id));
        repository.delete(entity);
    }

}