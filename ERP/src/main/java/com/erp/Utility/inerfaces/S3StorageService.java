package com.erp.Utility.inerfaces;

import com.erp.Dto.Response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface S3StorageService {
    FileUploadResponse uploadFile(MultipartFile file,
                                  String tenantName,
                                  Map<String, String> metadata);

    byte[] downloadFile(String tenant, String key);

}