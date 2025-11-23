package com.erp.Service.DebitNote;

import com.erp.Dto.Request.DebitNoteRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateDebitNoteRequestDTO;
import com.erp.Dto.Response.DebitNoteResponseDTO;
import com.erp.Dto.Response.ResultDto;

public interface DebitNoteService {

    DebitNoteResponseDTO createDebitNote(DebitNoteRequestDTO dto);

    ResultDto<DebitNoteResponseDTO> getFilteredDebitNotes(FilterRequest filterRequest);

    DebitNoteResponseDTO getDebitNoteById(Long id);

    DebitNoteResponseDTO updateDebitNote(Long id, UpdateDebitNoteRequestDTO dto);

    void deactivateDebitNote(Long id);

}
