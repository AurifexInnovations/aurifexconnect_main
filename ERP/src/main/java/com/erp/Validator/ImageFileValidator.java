package com.erp.Validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("imageValidator")
public class ImageFileValidator implements FileTypeValidator {

    private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/png", "image/jpg"};

    @Override
    public boolean isValid(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) return false;
        for (String type : ALLOWED_TYPES) {
            if (contentType.equalsIgnoreCase(type)) return true;
        }
        return false;
    }
}
