package com.erp.Utility;

import com.erp.Dto.Response.FileUploadResponse;
import com.erp.Model.FileInfoDto;
import com.erp.Multitenancy.TenantContext;
import com.erp.Utility.inerfaces.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageServiceImpl implements S3StorageService {

    private final S3Client s3Client;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "png", "jpg", "jpeg"
    );

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/png",
            "image/jpeg"
    );


    @Value("${aws.s3.bucket}")
    private String bucket;


    @Override
    public List<FileUploadResponse> uploadFile(
            MultipartFile[] files,
            String subPath) {

        String tenantPrefix = TenantContext.getCurrentTenant();
        String subPathPrefix = subPath.trim();

        if (tenantPrefix.isEmpty() || subPathPrefix.isEmpty()) {
            throw new IllegalArgumentException("tenantName and subPath are required");
        }

        ensureTenantPrefixExists(tenantPrefix);

        List<FileUploadResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) continue;

            byte[] bytes;
            try {
                bytes = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file", e);
            }

            validateImage(file, bytes);

            String cleanFileName = sanitizeFileName(file.getOriginalFilename());
            String key = tenantPrefix + "/" + subPathPrefix + "/"
                    + UUID.randomUUID()
                    + "_" + cleanFileName;

            // Optional: add system metadata per file
            Map<String, String> objectMetadata = new LinkedHashMap<>();

            objectMetadata.put("original-filename", cleanFileName);
            objectMetadata.put("tenant", tenantPrefix);
            objectMetadata.put("sub-path", subPathPrefix);

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .metadata(objectMetadata)
                    .build();

            s3Client.putObject(
                    putRequest,
                    RequestBody.fromBytes(bytes)
            );

            String s3Url = s3Client.utilities()
                    .getUrl(b -> b.bucket(bucket).key(key))
                    .toExternalForm();

            responses.add(
                    new FileUploadResponse(s3Url, key, cleanFileName)
            );
        }

        return responses;
    }


    @Override
    public List<FileUploadResponse> uploadFile(
            List<FileInfoDto> files,
            String subPath) {

        String tenantPrefix = TenantContext.getCurrentTenant();
        String subPathPrefix = subPath.trim();

        if (tenantPrefix.isEmpty() || subPathPrefix.isEmpty()) {
            throw new IllegalArgumentException("tenantName and subPath are required");
        }

        ensureTenantPrefixExists(tenantPrefix);

        List<FileUploadResponse> responses = new ArrayList<>();

        for (FileInfoDto file : files) {

            if (file == null || file.getFileData() == null) continue;

            byte[] bytes = convertBase64ToBytes(file.getFileData());

            validateImage(
                    file.getFileName(),
                    file.getFileType(),
                    bytes
            );


            String cleanFileName = sanitizeFileName(file.getFileName());
            String key = tenantPrefix + "/" + subPathPrefix + "/"
                    + UUID.randomUUID()
                    + "_" + cleanFileName;

            Map<String, String> objectMetadata = new LinkedHashMap<>();
            objectMetadata.put("original-filename", cleanFileName);
            objectMetadata.put("tenant", tenantPrefix);
            objectMetadata.put("sub-path", subPathPrefix);

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getFileType())
                    .metadata(objectMetadata)
                    .build();

            s3Client.putObject(
                    putRequest,
                    RequestBody.fromBytes(bytes)
            );

            String s3Url = s3Client.utilities()
                    .getUrl(b -> b.bucket(bucket).key(key))
                    .toExternalForm();

            responses.add(new FileUploadResponse(s3Url, key, cleanFileName));
        }

        return responses;
    }



    @Override
    public byte[] downloadFile(String tenantName, String key) {
        String tenantPrefix = tenantName == null ? "" : tenantName.trim();
        if (!tenantPrefix.isEmpty() && !tenantPrefix.endsWith("/")) {
            tenantPrefix = tenantPrefix + "/";
        }
        String fullKey = tenantPrefix + key;

        try {
            GetObjectRequest getReq = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(fullKey)
                    .build();

            ResponseBytes<GetObjectResponse> resp = s3Client.getObject(getReq, ResponseTransformer.toBytes());
            return resp.asByteArray();
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                log.warn("S3 object not found: {}/{}", bucket, fullKey);
                return null;
            }
            log.error("Error downloading from S3 {}/{}: {}", bucket, fullKey, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error downloading {}: {}", fullKey, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void ensureTenantPrefixExists(String tenantPrefix) {
        String folderKey = tenantPrefix.endsWith("/") ? tenantPrefix : tenantPrefix + "/";
        try {
            HeadObjectRequest headReq = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(folderKey)
                    .build();
            s3Client.headObject(headReq);
            // folder object exists
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                // create zero-byte object to represent folder
                PutObjectRequest putReq = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(folderKey)
                        .build();
                s3Client.putObject(putReq, RequestBody.fromBytes(new byte[0]));
                log.info("Created tenant folder prefix: {}", folderKey);
            } else {
                // other error rethrow
                throw ex;
            }
            //

        }
    }

    private static String sanitizeFileName(String original) {
        if (original == null || original.isBlank()) return "file";
        String name = original.trim();
        // remove path separators if present
        name = name.replaceAll("[\\\\/]", "");
        // replace whitespace runs with underscore (use "" to remove spaces)
        name = name.replaceAll("\\s+", "");
        // keep only safe filename characters (letters, digits, dot, underscore, hyphen)
        name = name.replaceAll("[^A-Za-z0-9._-]", "");
        // limit length
        if (name.length() > 100) name = name.substring(0, 100);
        return name;
    }

    private static void validateImage(MultipartFile file, byte[] bytes) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        /* ---------- Extension ---------- */
        String name = file.getOriginalFilename();
        if (name == null || !name.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Only PNG, JPG, JPEG files are allowed");
        }

        /* ---------- Content-Type ---------- */
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid content type");
        }

        /* ---------- Magic Bytes ---------- */
        if (bytes.length < 8) {
            throw new IllegalArgumentException("Invalid image file");
        }

        if (bytes.length > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 10 MB");
        }

        boolean isPng =
                (bytes[0] & 0xFF) == 0x89 &&
                        bytes[1] == 0x50 &&
                        bytes[2] == 0x4E &&
                        bytes[3] == 0x47 &&
                        bytes[4] == 0x0D &&
                        bytes[5] == 0x0A &&
                        bytes[6] == 0x1A &&
                        bytes[7] == 0x0A;

        boolean isJpeg =
                (bytes[0] & 0xFF) == 0xFF &&
                        (bytes[1] & 0xFF) == 0xD8 &&
                        (bytes[2] & 0xFF) == 0xFF;

        if (!isPng && !isJpeg) {
            throw new IllegalArgumentException("Invalid image signature");
        }
    }

    private static void validateImage(String fileName, String contentType, byte[] bytes) {

        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("File is empty");
        }

        /* ---------- Extension ---------- */
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Only PNG, JPG, JPEG files are allowed");
        }

        /* ---------- Content-Type ---------- */
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Invalid content type");
        }

        /* ---------- Magic Bytes ---------- */
        if (bytes.length < 8) {
            throw new IllegalArgumentException("Invalid image file");
        }

        if (bytes.length > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 10 MB");
        }

        boolean isPng =
                (bytes[0] & 0xFF) == 0x89 &&
                        bytes[1] == 0x50 &&
                        bytes[2] == 0x4E &&
                        bytes[3] == 0x47 &&
                        bytes[4] == 0x0D &&
                        bytes[5] == 0x0A &&
                        bytes[6] == 0x1A &&
                        bytes[7] == 0x0A;

        boolean isJpeg =
                (bytes[0] & 0xFF) == 0xFF &&
                        (bytes[1] & 0xFF) == 0xD8 &&
                        (bytes[2] & 0xFF) == 0xFF;

        if (!isPng && !isJpeg) {
            throw new IllegalArgumentException("Invalid image signature");
        }
    }

    private static byte[] convertBase64ToBytes(String base64Data) {

        if (base64Data == null || base64Data.isBlank()) {
            throw new IllegalArgumentException("Base64 data is empty");
        }

        // Handle data URI format: data:image/png;base64,xxxx
        if (base64Data.contains(",")) {
            base64Data = base64Data.substring(base64Data.indexOf(',') + 1);
        }

        try {
            return Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 encoding", e);
        }
    }

}