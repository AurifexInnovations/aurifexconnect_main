package com.erp.Strategy;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadStrategy {
    boolean supports(String category);   // check if this strategy handles given category
    List<String> uploadFiles(int seq , Long genId, String category, MultipartFile[] files);
    boolean validateId(Long genId);      // check if the given ID exists for the category
}
