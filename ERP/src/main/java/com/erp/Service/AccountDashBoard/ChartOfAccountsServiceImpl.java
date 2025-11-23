package com.erp.Service.AccountDashBoard;

import com.erp.Dto.ChartOfAccountsDTO;
import com.erp.Mapper.AccountDashBoard.ChartOfAccountsMapper;
import com.erp.Model.ChartOfAccounts;
import com.erp.Repository.ChartOfAccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChartOfAccountsServiceImpl implements IChartOfAccountsService {

    @Autowired
    private ChartOfAccountsRepository repository;

    ChartOfAccountsMapper c = new ChartOfAccountsMapper();

    @Override
    public ChartOfAccountsDTO getById(Integer id) {
        ChartOfAccounts entity = repository.findById(id).orElse(null);
        return entity != null ? c.toDto(entity) : null;
    }

    @Override
    public ChartOfAccountsDTO create(ChartOfAccountsDTO dto) {
        ChartOfAccounts entity = c.toEntity(dto);
        return c.toDto(repository.save(entity));
    }

    @Override
    public ChartOfAccountsDTO update(Integer id, ChartOfAccountsDTO dto) {
        ChartOfAccounts entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chart Of Account not found with id: " + id));

        // Update fields from DTO
        entity.setAccountName(dto.getAccountName());
        entity.setAccountType(dto.getAccountType());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setCoaId(dto.getCoaId());
        entity.setIsActive(dto.getIsActive());


        ChartOfAccounts updated = repository.save(entity);
        return c.toDto(updated);
    }

    @Override
    public void delete(Integer id) {
        ChartOfAccounts entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chart Of Account not found with id: " + id));
        repository.delete(entity);
    }
}