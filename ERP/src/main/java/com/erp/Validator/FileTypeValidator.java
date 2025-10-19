package com.erp.Validator;

import org.springframework.web.multipart.MultipartFile;

public interface FileTypeValidator {
    boolean isValid(MultipartFile file);
}
