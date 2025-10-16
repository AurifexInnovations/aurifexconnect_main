package com.erp.Service.Invoice;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String toEmail, byte[] pdfBytes )throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);

        messageHelper.setFrom(fromEmail);
        messageHelper.setTo(toEmail);
        messageHelper.setSubject("Your Invoice from \"YourCompanyName\"");
        messageHelper.setText("Dear Customer, Please Find the Attachment, It contains Your Invoice");

        DataSource dataSource = new ByteArrayDataSource(pdfBytes,"application/pdf");

        messageHelper.addAttachment("Invoice.pdf",dataSource);

        mailSender.send(message);
    }
}
