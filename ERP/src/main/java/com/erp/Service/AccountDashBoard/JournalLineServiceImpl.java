package com.erp.Service.AccountDashBoard;

import com.erp.Dto.JournalLineDTO;
import com.erp.Mapper.AccountDashBoard.JournalLineMapper;
import com.erp.Model.JournalLine;
import com.erp.Repository.JournalLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalLineServiceImpl implements IJournalLineService {

    @Autowired
    private JournalLineRepository repository;

    JournalLineMapper jlm = new JournalLineMapper();

    @Override
    public JournalLineDTO getById(Integer id) {
        JournalLine entity = repository.findById(id).orElse(null);
        return entity != null ? jlm.toDto(entity) : null;
    }

    @Override
    public JournalLineDTO create(JournalLineDTO dto) {
        JournalLine entity = jlm.toEntity(dto);
        return jlm.toDto(repository.save(entity));
    }

    @Override
    public JournalLineDTO update(Integer id, JournalLineDTO dto) {
        JournalLine entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal Line not found with id: " + id));

        // Update fields from DTO
        entity.setJournalId(dto.getJournalId());
        entity.setCoaId(dto.getCoaId());
        entity.setDebit(dto.getDebit());
        entity.setCredit(dto.getCredit());
        entity.setLineDescription(dto.getLineDescription());

        JournalLine updated = repository.save(entity);
        return jlm.toDto(updated);
    }

    @Override
    public void delete(Integer id) {
        JournalLine entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal Line not found with id: " + id));
        repository.delete(entity);
    }

}