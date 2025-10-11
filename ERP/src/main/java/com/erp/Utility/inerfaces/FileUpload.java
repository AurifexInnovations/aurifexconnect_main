package com.erp.Utility.inerfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpload {
    void uploadFile( String category , long id, MultipartFile[] files);
}
