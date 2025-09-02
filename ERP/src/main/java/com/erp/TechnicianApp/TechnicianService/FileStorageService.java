package com.erp.TechnicianApp.TechnicianService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads/location/";

    public String saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return null;
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            return "/uploads/location/" + fileName; // relative URL
        } catch (IOException e) {
            throw new RuntimeException("File storage failed", e);
        }
    }
}