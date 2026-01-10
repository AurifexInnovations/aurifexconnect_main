package com.erp.Controller.AccountDashboardView;

import com.erp.Dto.JournalDTO;
import com.erp.Service.AccountDashBoard.IJournalService;
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
@RequestMapping("/api/v1/journals")
@Tag(name = "Journal Controller", description = "APIs for Journals")
@AllArgsConstructor
public class JournalController {

    @Autowired
    IJournalService journalService;

    @GetMapping("/journal-by-id/{journalId}")
    @Operation(description = "Fetch Journal by ID",
            responses = {@ApiResponse(responseCode = "200", description = "Journal retrieved")})
    public ResponseEntity<ResponseStructure<JournalDTO>> fetchById(@PathVariable Integer journalId) {
        JournalDTO response = journalService.getById(journalId);
        return ResponseBuilder.success(HttpStatus.OK, "Journal retrieved successfully", response);
    }

    @PostMapping("/create-journal")
    @Operation(description = "Create Journal",
            responses = {@ApiResponse(responseCode = "201", description = "Journal created")})
    public ResponseEntity<ResponseStructure<JournalDTO>> create(@Valid @RequestBody JournalDTO dto) {
        JournalDTO response = journalService.create(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Journal created successfully", response);
    }

    @PutMapping("/update-journal")
    @Operation(description = "Update Journal",
            responses = {@ApiResponse(responseCode = "200", description = "Journal updated")})
    public ResponseEntity<ResponseStructure<JournalDTO>> update(
                                                                @Valid @RequestBody JournalDTO dto) {
        JournalDTO response = journalService.update(dto.getJournalId(), dto);
        return ResponseBuilder.success(HttpStatus.OK, "Journal updated successfully", response);
    }

    @DeleteMapping("/{journalId}")
    @Operation(description = "Delete Journal",
            responses = {@ApiResponse(responseCode = "204", description = "Journal deleted")})
    public ResponseEntity<ResponseStructure<Void>> delete(@PathVariable Integer journalId) {
        journalService.delete(journalId);
        return (ResponseEntity<ResponseStructure<Void>>) (ResponseEntity<?>) ResponseBuilder.success(
                HttpStatus.NO_CONTENT,
                "Journal deleted successfully",
                null
        );
    }
}