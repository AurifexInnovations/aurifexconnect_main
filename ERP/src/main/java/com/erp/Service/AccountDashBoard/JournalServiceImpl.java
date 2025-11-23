package com.erp.Service.AccountDashBoard;

import com.erp.Dto.JournalDTO;
import com.erp.Mapper.AccountDashBoard.JournalMapper;
import com.erp.Model.Journal;
import com.erp.Repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalServiceImpl implements IJournalService {

    @Autowired
    private JournalRepository repository;

    JournalMapper jm = new JournalMapper();
    
    @Override
    public JournalDTO getById(Integer id) {
        Journal entity = repository.findById(id).orElse(null);
        return entity != null ? jm.toDto(entity) : null;
    }

    @Override
    public JournalDTO create(JournalDTO dto) {
        Journal entity = jm.toEntity(dto);
        return jm.toDto(repository.save(entity));
    }

    @Override
    public JournalDTO update(Integer id, JournalDTO dto) {
        Journal entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found with id: " + id));

        // Update fields from DTO
        entity.setJournalId(dto.getJournalId());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setReferenceId(dto.getReferenceId());
        entity.setReferenceType(dto.getReferenceType());

        Journal updated = repository.save(entity);
        return jm.toDto(updated);
    }

    @Override
    public void delete(Integer id) {
        Journal entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found with id: " + id));
        repository.delete(entity);
    }

}