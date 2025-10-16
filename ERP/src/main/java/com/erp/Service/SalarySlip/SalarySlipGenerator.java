package com.erp.Service.SalarySlip;

import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Service.SalaryService.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;


@Service
@RequiredArgsConstructor
public class SalarySlipGenerator {

    private final SpringTemplateEngine templateEngine;
    private final SalaryService salaryService;

    public byte[] generateSlipPdf(SalaryRequest request) {
        try {
            SalaryResponse salary = salaryService.getSalaryByUserAndMonth(request);

            Context context = new Context();
            context.setVariable("salary", salary);
            String html = templateEngine.process("salary-slip", context);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ITextRenderer render = new ITextRenderer();
            render.setDocumentFromString(html);
            render.layout();
            render.createPDF(baos, false);
            render.finishPDF();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating salary slip PDF: " + e.getMessage(), e);
        }
    }
}
