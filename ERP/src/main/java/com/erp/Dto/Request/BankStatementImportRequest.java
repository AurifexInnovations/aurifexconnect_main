package com.erp.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementImportRequest {

    @NotNull(message = "Bank account ID is required")
    private Long bankAccountId;

    @NotNull(message = "Statement date is required")
    private LocalDate statementDate;

    @NotNull(message = "File is required")
    private MultipartFile file;

    private String importSource; // CSV, EXCEL, OFX, QIF

    private String delimiter; // For CSV files

    private String notes;
}
