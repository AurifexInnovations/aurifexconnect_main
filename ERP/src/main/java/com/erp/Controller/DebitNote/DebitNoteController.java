package com.erp.Controller.DebitNote;

import com.erp.Dto.Request.DebitNoteRequestDTO;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.UpdateDebitNoteRequestDTO;
import com.erp.Dto.Response.DebitNoteResponseDTO;
import com.erp.Dto.Response.ResultDto;
import com.erp.Service.DebitNote.DebitNoteService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/v1/procurement/debit/notes")
public class DebitNoteController {

    @Autowired
    private DebitNoteService debitNoteService;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ResponseStructure<DebitNoteResponseDTO>> createDebitNote(
            @RequestBody @Valid DebitNoteRequestDTO dto) {

        DebitNoteResponseDTO response = debitNoteService.createDebitNote(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.<DebitNoteResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Debit Note created successfully")
                        .data(response)
                        .build());
    }

    // FILTER LIST
    @PostMapping("/filter")
    public ResponseEntity<ResponseStructure<ResultDto<DebitNoteResponseDTO>>> getDebitNotes(
            @RequestBody FilterRequest filterRequest) {

        ResultDto<DebitNoteResponseDTO> result =
                debitNoteService.getFilteredDebitNotes(filterRequest);

        return ResponseBuilder.success(HttpStatus.OK,
                "Debit Notes retrieved successfully", result);
    }

    // DETAIL
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseStructure<DebitNoteResponseDTO>> getDebitNote(
            @PathVariable Long id) {

        DebitNoteResponseDTO response = debitNoteService.getDebitNoteById(id);

        return ResponseEntity.ok(
                ResponseStructure.<DebitNoteResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Debit Note details fetched")
                        .data(response)
                        .build());
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseStructure<DebitNoteResponseDTO>> updateDebitNote(
            @PathVariable Long id,
            @RequestBody UpdateDebitNoteRequestDTO dto) {

        DebitNoteResponseDTO response = debitNoteService.updateDebitNote(id, dto);

        return ResponseEntity.ok(
                ResponseStructure.<DebitNoteResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Debit Note updated successfully")
                        .data(response)
                        .build());
    }

    // DEACTIVATE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteDebitNote(@PathVariable Long id) {

        debitNoteService.deactivateDebitNote(id);

        return ResponseEntity.ok(
                ResponseStructure.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Debit Note deactivated successfully")
                        .data("Debit Note has been marked inactive")
                        .build());
    }
}
