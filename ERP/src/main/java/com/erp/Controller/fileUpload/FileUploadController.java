package com.erp.Controller.fileUpload;

import com.erp.Service.FileService.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileUploadController {

    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = fileService.uploadFile(file);
        return ResponseEntity.ok("File uploaded: " + fileUrl);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        byte[] fileContent = fileService.downloadFile(fileName);
        if (fileContent != null) {
            return ResponseEntity.ok().body(fileContent);
        }
        return ResponseEntity.notFound().build();
    }
}