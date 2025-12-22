package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse {
    private String s3Url;
    private String s3Key;
    private String fileName;
}