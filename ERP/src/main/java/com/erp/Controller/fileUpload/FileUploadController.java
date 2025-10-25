package com.erp.Controller.fileUpload;

import com.erp.Dto.Request.FileRequestDto;
import com.erp.Dto.Response.FileUploadResponse;
import com.erp.Utility.inerfaces.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class FileUploadController {
    private final S3StorageService storageService;

    @PostMapping("/{tenant}/files")
    public ResponseEntity<FileUploadResponse> uploadFile(@PathVariable("tenant") final String tenant,
                                                         @RequestPart("file") final MultipartFile file,
                                                         @RequestParam final MultiValueMap<String, String> params) {
        // optional additional metadata as request params; adapt as needed
        Map<String, String> metadata = new HashMap<>();
        params.forEach((k, v) -> metadata.put(k, v.get(0)));

        FileUploadResponse resp = storageService.uploadFile(file, tenant, metadata);
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/{tenant}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("tenant") final String tenant,
                                               @RequestBody final FileRequestDto requestDto) {
        byte[] data = storageService.downloadFile(tenant, requestDto.file());
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + requestDto.file() + "\"").contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length).body(data);
    }
}