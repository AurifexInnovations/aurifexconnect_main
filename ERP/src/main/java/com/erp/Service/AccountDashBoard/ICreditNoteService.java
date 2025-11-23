package com.erp.Service.AccountDashBoard;

import com.erp.Dto.CreditNoteDTO;

public interface ICreditNoteService {
    CreditNoteDTO getById(Integer id);
    CreditNoteDTO create(CreditNoteDTO dto);
    CreditNoteDTO update(CreditNoteDTO dto);
    void delete(Integer id);

}