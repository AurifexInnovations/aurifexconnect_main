package com.erp.Controller.AccountDashboardView;

import com.erp.Dto.JournalLineDTO;
import com.erp.Service.AccountDashBoard.IJournalLineService;
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
@RequestMapping("/api/v1/journal-lines")
@Tag(name = "Journal Line Controller", description = "APIs for Journal Lines")
@AllArgsConstructor
public class JournalLineController {

    @Autowired
    private IJournalLineService journalLineService;

    @GetMapping("/{journalLineId}")
    @Operation(description = "Fetch Journal Line by ID",
            responses = {@ApiResponse(responseCode = "200", description = "Journal Line retrieved")})
    public ResponseEntity<ResponseStructure<JournalLineDTO>> fetchById(@PathVariable Integer journalLineId) {
        JournalLineDTO response = journalLineService.getById(journalLineId);
        return ResponseBuilder.success(HttpStatus.OK, "Journal Line retrieved successfully", response);
    }

    @PostMapping("/create-journal-line")
    @Operation(description = "Create Journal Line",
            responses = {@ApiResponse(responseCode = "201", description = "Journal Line created")})
    public ResponseEntity<ResponseStructure<JournalLineDTO>> create(@Valid @RequestBody JournalLineDTO dto) {
        JournalLineDTO response = journalLineService.create(dto);
        return ResponseBuilder.success(HttpStatus.CREATED, "Journal Line created successfully", response);
    }

    @PutMapping("/update-journal-line")
    @Operation(description = "Update Journal Line",
            responses = {@ApiResponse(responseCode = "200", description = "Journal Line updated")})
    public ResponseEntity<ResponseStructure<JournalLineDTO>> update(
                                                                    @Valid @RequestBody JournalLineDTO dto) {
        JournalLineDTO response = journalLineService.update(dto.getJournalId(), dto);
        return ResponseBuilder.success(HttpStatus.OK, "Journal Line updated successfully", response);
    }

    @DeleteMapping("/{journalLineId}")
    @Operation(description = "Delete Journal Line",
            responses = {@ApiResponse(responseCode = "204", description = "Journal Line deleted")})
    public ResponseEntity<ResponseStructure<Void>> delete(@PathVariable Integer journalLineId) {
        journalLineService.delete(journalLineId);
        return (ResponseEntity<ResponseStructure<Void>>) (ResponseEntity<?>) ResponseBuilder.success(
                HttpStatus.NO_CONTENT,
                "Journal Line deleted successfully", null
        );
    }
//

}