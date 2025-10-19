package com.erp.Validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("pdfValidator")
public class PdfFileValidator implements FileTypeValidator {

    @Override
    public boolean isValid(MultipartFile file) {
        return "application/pdf".equalsIgnoreCase(file.getContentType());
    }
}
