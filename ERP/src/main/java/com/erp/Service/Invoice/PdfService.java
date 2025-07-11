package com.erp.Service.Invoice;

import com.erp.Dto.Request.InvoiceRequest;
import com.erp.Model.InvoiceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final SpringTemplateEngine templateEngine;
    private final InvoiceService invoiceService;


    public byte[] generateInvoicePdf(InvoiceRequest request){
        try {

            InvoiceGenerator invoice = invoiceService.fetchInvoice(request);

            //prepare thymeleaf context
            Context context = new Context();
            context.setVariable("invoice", invoice);


            //Render Thymeleaf Template
            String html = templateEngine.process("invoice-preview", context);

            //prepare ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ITextRenderer render = new ITextRenderer();
            render.setDocumentFromString(html);
            render.layout();
            render.createPDF(baos, false);
            render.finishPDF();

            return baos.toByteArray();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

}
