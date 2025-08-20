package com.erp.Service.Lead;

import com.erp.Dto.Request.LeadRequest;
import com.erp.Dto.Response.LeadResponse;
import com.erp.Exception.LeadImportExportException.ExcelExportException;
import com.erp.Exception.LeadImportExportException.InvalidCSVException;
import com.erp.Exception.LeadImportExportException.PDFExportException;
import com.erp.Mapper.Lead.LeadMapper;
import com.erp.Model.Lead;
import com.erp.Repository.Lead.LeadRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LeadImportExportService {

    private final LeadRepository leadRepo;
    private final LeadMapper leadMapper;

    /**
     * Import leads from a CSV file
     */
    public List<LeadResponse> importFromCsv(MultipartFile file) throws IOException {
        List<LeadResponse> responses = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean firstRow = true;

            while ((line = reader.readNext()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                LeadRequest request = new LeadRequest();
                request.setFirstName(line[0]);
                request.setLastName(line[1]);
                request.setEmail(line[2]);
                request.setPhone(line[3]);
                request.setSource(line[4]);

                Lead lead = leadMapper.mapToEntity(request);
                leadRepo.save(lead);
                responses.add(leadMapper.mapToResponse(lead));
            }
        } catch (com.opencsv.exceptions.CsvValidationException e) {
            throw new InvalidCSVException("CSV file is not valid: " + e.getMessage());
        }

        return responses;
    }

    /**
     * Export leads to Excel (.xlsx)
     */
    public ByteArrayInputStream exportToExcel() {
        try {
            List<Lead> leads = leadRepo.findAll();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Leads");

            // Header
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone", "Source", "Status"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Data rows
            int rowIdx = 1;
            for (Lead lead : leads) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(lead.getId());
                row.createCell(1).setCellValue(lead.getFirstName());
                row.createCell(2).setCellValue(lead.getLastName());
                row.createCell(3).setCellValue(lead.getEmail());
                row.createCell(4).setCellValue(lead.getPhone());
                row.createCell(5).setCellValue(lead.getSource());
                row.createCell(6).setCellValue(lead.getStatus().name());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new ExcelExportException("Error exporting leads to Excel: " + e.getMessage());
        }
    }

    /**
     * Export leads to PDF
     */
    public ByteArrayInputStream exportToPdf() {
        List<Lead> leads = leadRepo.findAll();
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("CRM Leads Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Generated on: " + java.time.LocalDate.now()));
            document.add(Chunk.NEWLINE);

            // Table with 6 columns
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 3, 3, 4, 3, 2});

            // Header row
            Stream.of("ID", "First Name", "Last Name", "Email", "Phone", "Status")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);
                        header.setPhrase(new Phrase(headerTitle, headFont));
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(header);
                    });

            // Data rows
            for (Lead lead : leads) {
                table.addCell(String.valueOf(lead.getId()));
                table.addCell(lead.getFirstName());
                table.addCell(lead.getLastName());
                table.addCell(lead.getEmail());
                table.addCell(lead.getPhone());
                table.addCell(lead.getStatus().name());
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new PDFExportException("Error exporting leads to PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}