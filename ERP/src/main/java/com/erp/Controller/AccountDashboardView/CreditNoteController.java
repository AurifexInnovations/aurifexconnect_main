package com.erp.Controller.AccountDashboardView;

import com.erp.Dto.CreditNoteDTO;
import com.erp.Service.AccountDashBoard.ICreditNoteService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit-notes")
@Tag(name = "Credit Note Controller", description = "APIs for Credit Notes")
@AllArgsConstructor
public class CreditNoteController {

    @Autowired
    private ICreditNoteService creditNoteService;

    @GetMapping("/{creditNoteId}")
    @Operation(description = "Fetch Credit Note by ID",
            responses = {@ApiResponse(responseCode = "200", description = "Credit Note retrieved")})
    public ResponseEntity<ResponseStructure<CreditNoteDTO>> fetchById(@PathVariable Integer creditNoteId) {
        CreditNoteDTO response = creditNoteService.getById(creditNoteId);
        return ResponseBuilder.success(HttpStatus.OK, "Credit Note retrieved successfully", response);
    }

    @PostMapping
    @Operation(description = "Create Credit Note",
            responses = {@ApiResponse(responseCode = "201", description = "Credit Note created")})
    public ResponseEntity<ResponseStructure<CreditNoteDTO>> create(@Valid @RequestBody CreditNoteDTO dto) {
        CreditNoteDTO response = creditNoteService.create(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Credit Note created successfully", response);
    }

    @PutMapping("/update-credit-note")
    @Operation(description = "Update Credit Note",
            responses = {@ApiResponse(responseCode = "200", description = "Credit Note updated")})
    public ResponseEntity<ResponseStructure<CreditNoteDTO>> update(
                                                                   @Valid @RequestBody CreditNoteDTO dto) {
        CreditNoteDTO response = creditNoteService.update(dto);
        return ResponseBuilder.success(HttpStatus.OK, "Credit Note updated successfully", response);
    }

    @DeleteMapping("/{creditNoteId}")
    @Operation(description = "Delete Credit Note",
            responses = {@ApiResponse(responseCode = "204", description = "Credit Note deleted")})
    public ResponseEntity<ResponseStructure<Void>> delete(@PathVariable Integer creditNoteId) {
        creditNoteService.delete(creditNoteId);

        return (ResponseEntity<ResponseStructure<Void>>) (ResponseEntity<?>) ResponseBuilder.success(
                HttpStatus.NO_CONTENT,
                "Credit Note deleted successfully",
                null
        );

    }

}