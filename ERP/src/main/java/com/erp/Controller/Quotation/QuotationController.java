package com.erp.Controller.Quotation;

import com.erp.Dto.Request.QuotationRequest;
import com.erp.Dto.Response.QuotationResponse;
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
    public ResponseEntity<ResponseStructure<QuotationResponse>> create(@RequestBody QuotationRequest quotation) {
        QuotationResponse quotationResponse = quotationService.createQuotation(quotation);
        return ResponseBuilder.success(HttpStatus.CREATED, "Quotation Created", quotationResponse);
    }

    // 🔹 2. Get all quotations
    @GetMapping("/all")
    public ResponseEntity<ResponseStructure<ResultDto<QuotationResponse>>> getAll() {
        ResultDto<QuotationResponse> resultDto = quotationService.getAllQuotations();
        return ResponseBuilder.success(HttpStatus.OK, "All Quatation Fetched", resultDto);
    }

    // 🔹 3. Get single quotation
    @GetMapping()
    public ResponseEntity<ResponseStructure<QuotationResponse>> getOne(@RequestParam String quotationId) {
        QuotationResponse quotationResponse = quotationService.getQuotationById(quotationId);
        return ResponseBuilder.success(HttpStatus.OK, "Quotation Fetched", quotationResponse);
    }

    // 🔹 4. Update quotation
    @PutMapping()
    public ResponseEntity<ResponseStructure<QuotationResponse>> update(@RequestBody QuotationRequest quotation) {
        QuotationResponse quotationResponse = quotationService.updateQuotation(quotation);
        return ResponseBuilder.success(HttpStatus.OK, "Quotation Updated", quotationResponse);
    }

    // 🔹 5. Delete quotation
    @DeleteMapping()
    public ResponseEntity<ResponseStructure<String>> delete(@RequestParam String quotationId) {
        quotationService.deleteQuotation(quotationId);
        return ResponseBuilder.success(HttpStatus.OK, "Deleted Quotation With Id : " + quotationId, "DELETE");
    }

    // ======================================
    // 🔹 6. Convert quotation (e.g. to invoice/job)
    // ======================================
    @PostMapping("/convert")
    public ResponseEntity<ResponseStructure<Map<String, Object>>> convertQuotation(@RequestBody Map<String, String> request) {
        String quotationId = request.get("quotationId");
        String targetType = request.get("targetType");

        Map<String, Object> data = Map.of(
                "quotationId", quotationId,
                "convertedTo", targetType
        );

        return ResponseBuilder.success(HttpStatus.OK, "Quotation Converted Successfully", data);
    }

    // ======================================
    // 🔹 7. Send quotation (email, etc.)
    // ======================================
    @PostMapping("/send")
    public ResponseEntity<ResponseStructure<Map<String, Object>>> sendQuotation(@RequestBody Map<String, String> request) {
        String quotationId = request.get("quotationId");
        String method = request.getOrDefault("method", "email");

        Map<String, Object> data = Map.of(
                "quotationId", quotationId,
                "status", "200",
                "method", method
        );

        return ResponseBuilder.success(HttpStatus.OK, "Quotation Sended Successfully", data);
    }

    // ======================================
    // 🔹 8. Translate quotation
    // ======================================
    @PostMapping("/translate")
    public ResponseEntity<ResponseStructure<Map<String, Object>>> translateQuotation(@RequestBody Map<String, String> request) {
        String quotationId = request.get("quotationId");
        String targetLanguage = request.get("targetLanguage");

        Map<String, Object> data = Map.of(
                "quotationId", quotationId,
                "translatedTo", targetLanguage
        );

        return ResponseBuilder.success(HttpStatus.OK, "Quotation Translated Successfully", data);
    }

    // ======================================
    // 🔹 9. AI generate quotation (mock)
    // ======================================
    @PostMapping("/ai-generate")
    public ResponseEntity<ResponseStructure<Map<String, Object>>> aiGenerateQuotation(@RequestBody Map<String, Object> request) {
        String customerId = (String) request.get("customerId");
        String serviceType = (String) request.get("serviceType");

        Map<String, Object> data = Map.of(
                "quotationId", "AI-QTN-" + System.currentTimeMillis(),
                "status", "draft",
                "type", "AI Generated",
                "totalAmount", 27500,
                "customerId", customerId,
                "serviceType", serviceType
        );

        return ResponseBuilder.success(HttpStatus.OK, "AI-generated quotation created successfully", data);
    }
}