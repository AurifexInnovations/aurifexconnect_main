package com.erp.Service.AccountDashBoard;

import com.erp.Dto.JournalLineDTO;

public interface IJournalLineService {
    JournalLineDTO getById(Integer id);
    JournalLineDTO create(JournalLineDTO dto);
    JournalLineDTO update(Integer id, JournalLineDTO dto);
    void delete(Integer id);

}
