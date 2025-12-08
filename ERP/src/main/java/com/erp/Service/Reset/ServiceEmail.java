package com.erp.Service.Reset;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ServiceEmail {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");

            String html = "<h2>Your OTP is: <b>" + otp + "</b></h2>"
                    + "<p>Valid for <b>5 minutes</b>.</p>";

            helper.setText(html, true);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
