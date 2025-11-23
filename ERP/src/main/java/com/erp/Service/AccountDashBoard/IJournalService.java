package com.erp.Service.AccountDashBoard;

import com.erp.Dto.JournalDTO;

public interface IJournalService {
    JournalDTO getById(Integer id);
    JournalDTO create(JournalDTO dto);
    JournalDTO update(Integer id, JournalDTO dto);
    void delete(Integer id);
}