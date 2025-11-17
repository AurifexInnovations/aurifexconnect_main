package com.erp.Controller.Quotation;

import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Quotation;
import com.erp.Service.Quotation.QuotationService;
import com.erp.Utility.ResponseBuilder;
import com.erp.Utility.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    // 🔹 1. Create quotation
    @PostMapping("/")
    public Quotation create(@RequestBody Quotation quotation) {
        return quotationService.createQuotation(quotation);
    }

    // 🔹 2. Get all quotations
    @GetMapping("/all")
    public ResponseEntity<ResponseStructure<ResultDto<Quotation>>> getAll() {
        ResultDto<Quotation> resultDto = quotationService.getAllQuotations();
        return ResponseBuilder.success(HttpStatus.OK, "All Quatation Fetched", resultDto);
    }

    // 🔹 3. Get single quotation
    @GetMapping("/{quotationId}")
    public Quotation getOne(@PathVariable String quotationId) {
        return quotationService.getQuotationById(quotationId);
    }

    // 🔹 4. Update quotation
    @PutMapping("/{quotationId}")
    public Quotation update(@PathVariable String quotationId, @RequestBody Quotation quotation) {
        return quotationService.updateQuotation(quotationId, quotation);
    }

    // 🔹 5. Delete quotation
    @DeleteMapping("/{quotationId}")
    public String delete(@PathVariable String quotationId) {
        quotationService.deleteQuotation(quotationId);
        return "Deleted Successfully";
    }

    // ======================================
    // 🔹 6. Convert quotation (e.g. to invoice/job)
    // ======================================
    @PostMapping("/convert")
    public Map<String, Object> convertQuotation(@RequestBody Map<String, String> request) {
        String quotationId = request.get("quotationId");
        String targetType = request.get("targetType");
        return Map.of(
                "quotationId", quotationId,
                "convertedTo", targetType,
                "message", "Quotation converted successfully"
        );
    }

    // ======================================
    // 🔹 7. Send quotation (email, etc.)
    // ======================================
    @PostMapping("/send")
    public Map<String, Object> sendQuotation(@RequestBody Map<String, String> request) {
        String quotationId = request.get("quotationId");
        String method = request.getOrDefault("method", "email");
        return Map.of(
                "quotationId", quotationId,
                "status", "sent",
                "method", method,
                "message", "Quotation sent successfully via " + method
        );
    }

    // ======================================
    // 🔹 8. Translate quotation
    // ======================================
    @PostMapping("/translate")
    public Map<String, Object> translateQuotation(@RequestBody Map<String, String> request) {
        String quotationId = request.get("quotationId");
        String targetLanguage = request.get("targetLanguage");
        return Map.of(
                "quotationId", quotationId,
                "translatedTo", targetLanguage,
                "message", "Quotation translated successfully"
        );
    }

    // ======================================
    // 🔹 9. AI generate quotation (mock)
    // ======================================
    @PostMapping("/ai-generate")
    public Map<String, Object> aiGenerateQuotation(@RequestBody Map<String, Object> request) {
        String customerId = (String) request.get("customerId");
        String serviceType = (String) request.get("serviceType");

        return Map.of(
                "quotationId", "AI-QTN-" + System.currentTimeMillis(),
                "status", "draft",
                "type", "AI Generated",
                "totalAmount", 27500,
                "customerId", customerId,
                "serviceType", serviceType,
                "message", "AI-generated quotation created successfully"
        );
    }
}
