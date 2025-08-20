package com.erp.Controller.Lead;

import com.erp.Dto.Response.LeadResponse;
import com.erp.Service.Lead.LeadImportExportService;
import com.erp.Utility.ListResponseStructure;
import com.erp.Utility.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/crm/leads/import-export")
@RequiredArgsConstructor
public class LeadImportExportController {

    private final LeadImportExportService importExportService;

    @PostMapping("/csv")
    @Operation(description = "Import leads from a CSV file")
    public ResponseEntity<ListResponseStructure<LeadResponse>> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        List<LeadResponse> responses = importExportService.importFromCsv(file);
        return ResponseBuilder.success(HttpStatus.CREATED, "Leads imported successfully", responses);
    }

    @GetMapping("/excel")
    @Operation(description = "Export all leads to Excel")
    public ResponseEntity<byte[]> exportExcel() {
        ByteArrayInputStream stream = importExportService.exportToExcel();
        byte[] content = stream.readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leads.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(content);
    }

    @GetMapping("/pdf")
    @Operation(description = "Export all leads to PDF")
    public ResponseEntity<byte[]> exportPdf() {
        ByteArrayInputStream stream = importExportService.exportToPdf();
        byte[] content = stream.readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leads.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }
}