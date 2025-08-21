package com.erp.Controller.Invoice;


import com.erp.Dto.Request.InvoiceRequest;
import com.erp.Model.InvoiceGenerator;
import com.erp.Service.Invoice.EmailService;
import com.erp.Service.Invoice.InvoiceService;
import com.erp.Service.Invoice.PdfService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PdfService pdfService;
    private final EmailService emailService;

    @PostMapping("/create")
    public String invoicePreview(@RequestBody InvoiceRequest request,
                                 Model model){
        InvoiceGenerator invoiceGenerator = invoiceService.createInvoice(request);

        model.addAttribute("invoice",invoiceGenerator);
        return "invoice-preview";
    }

    @PostMapping("/find")
    public String findInvoice(@RequestBody InvoiceRequest request,
                              Model model)
    {
        InvoiceGenerator invoice = invoiceService.fetchInvoice(request);
        model.addAttribute("invoice",invoice);
        return "invoice-preview";
    }

    @PostMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody InvoiceRequest request){

        byte[] pdf = pdfService.generateInvoicePdf(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + request.getMasterId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    @PostMapping("/sendMail")
    public ResponseEntity<String> sendInvoiceEmail(@RequestBody InvoiceRequest request) throws MessagingException {

        InvoiceGenerator invoiceGenerator = invoiceService.fetchInvoice(request);

        String customerEmail = invoiceGenerator.getMaster().getLedger().getEmail();

        byte[] pdf = pdfService.generateInvoicePdf(request);

        emailService.sendEmail(customerEmail,pdf);

        return  ResponseEntity.ok("Email Sent To : "+customerEmail);

    }
}