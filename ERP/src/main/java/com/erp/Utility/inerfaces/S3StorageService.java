package com.erp.Utility.inerfaces;

import com.erp.Dto.Response.FileUploadResponse;
import com.erp.Model.File;
import com.erp.Model.FileInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface S3StorageService {
    List<FileUploadResponse> uploadFile(MultipartFile[] file,
                                        String subPath);

    byte[] downloadFile(String tenant, String key);

    public List<FileUploadResponse> uploadFile(
            List<FileInfoDto> files,
            String subPath);
}