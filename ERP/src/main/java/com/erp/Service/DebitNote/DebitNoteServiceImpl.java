package com.erp.Service.DebitNote;

import com.erp.CustomRepository.DebitNoteCustomRepository;
import com.erp.Dto.Request.DebitNoteRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateDebitNoteRequestDTO;
import com.erp.Dto.Response.DebitNoteResponseDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.DebitNote;
import com.erp.Repository.DebitNote.DebitNoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
public class DebitNoteServiceImpl implements DebitNoteService {

    @Autowired
    private DebitNoteRepository debitNoteRepository;

    @Autowired
    private DebitNoteCustomRepository debitNoteCustomRepository;

    @Override
    @Transactional
    public DebitNoteResponseDTO createDebitNote(DebitNoteRequestDTO dto) {

        log.info("START :: createDebitNote() DTO: {}", dto);

        DebitNote dn = new DebitNote();

        dn.setBillId(dto.getBillId());
        dn.setDateIssued(LocalDate.parse(dto.getDateIssued()));
        dn.setReason(dto.getReason());
        dn.setAmountDebited(dto.getAmountDebited());
        dn.setInventoryAdjustment(dto.getInventoryAdjustment());
        dn.setTaxAdjustmentAmount(dto.getTaxAdjustmentAmount());
        dn.setDnNumber("DN-" + System.currentTimeMillis());

        debitNoteRepository.save(dn);

        log.info("TRIGGER => Journal Entry for Debit Note [{}]", dn.getDnId());

        return mapToResponse(dn);
    }

    @Override
    public ResultDto<DebitNoteResponseDTO> getFilteredDebitNotes(FilterRequest filterRequest) {
        return debitNoteCustomRepository.getFilteredDebitNotes(filterRequest);
    }

    @Override
    public DebitNoteResponseDTO getDebitNoteById(Long id) {

        DebitNote dn = debitNoteRepository.findByDnIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Debit Note not found"));

        return mapToResponse(dn);
    }

    @Override
    @Transactional
    public DebitNoteResponseDTO updateDebitNote(Long id, UpdateDebitNoteRequestDTO dto) {

        DebitNote dn = debitNoteRepository.findByDnIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Debit Note not found"));

        if (dto.getReason() != null)
            dn.setReason(dto.getReason());

        debitNoteRepository.save(dn);

        return mapToResponse(dn);
    }

    @Override
    @Transactional
    public void deactivateDebitNote(Long id) {

        DebitNote dn = debitNoteRepository.findByDnIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Debit Note not found"));

        dn.setIsActive(false);
        debitNoteRepository.save(dn);
    }

    private DebitNoteResponseDTO mapToResponse(DebitNote dn) {

        DebitNoteResponseDTO dto = new DebitNoteResponseDTO();

        dto.setDnId(dn.getDnId());
        dto.setBillId(dn.getBillId());
        dto.setVendorId(dn.getVendorId());
        dto.setDateIssued(dn.getDateIssued().toString());
        dto.setReason(dn.getReason());
        dto.setAmountDebited(dn.getAmountDebited());
        dto.setDnNumber(dn.getDnNumber());
        dto.setInventoryAdjustment(dn.getInventoryAdjustment());
        dto.setTaxAdjustmentAmount(dn.getTaxAdjustmentAmount());
        dto.setStatus(dn.getStatus());

        return dto;
    }
}
