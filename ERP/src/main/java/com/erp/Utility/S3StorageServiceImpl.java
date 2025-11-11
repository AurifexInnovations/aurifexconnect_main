package com.erp.Utility;

import com.erp.Dto.Response.FileUploadResponse;
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

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageServiceImpl implements S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String tenantName, Map<String, String> metadata) {
        try {
            String tenantPrefix = tenantName.trim();
            if (tenantPrefix.isEmpty()) {
                throw new IllegalArgumentException("tenantName is required");
            }

            ensureTenantPrefixExists(tenantPrefix);
            final String cleanFileName = sanitizeFileName(file.getOriginalFilename());
            final String key = tenantPrefix + "/" + System.currentTimeMillis() + "_" + cleanFileName;

            Map<String, String> userMetadata = new HashMap<>();
            if (metadata != null) userMetadata.putAll(metadata);

            PutObjectRequest.Builder porBuilder = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType());

            if (!userMetadata.isEmpty()) {
                porBuilder = porBuilder.metadata(userMetadata);
            }

            PutObjectRequest putReq = porBuilder.build();

            s3Client.putObject(putReq, RequestBody.fromBytes(file.getBytes()));

            String s3Url;
            try {
                s3Url = s3Client.utilities()
                        .getUrl(builder -> builder.bucket(bucket).key(key))
                        .toExternalForm();
            } catch (Exception e) {
                // fallback to a constructed URL (may not work for all regions/setups)
                s3Url = String.format("https://%s.s3.amazonaws.com/%s", bucket, key);
            }

//            TenantFileMetadata saved = TenantFileMetadata.builder()
//                    .tenantName(tenantName)
//                    .fileName(cleanFileName)
//                    .s3Key(key)
//                    .contentType(file.getContentType())
//                    .size(file.getSize())
//                    .uploadedAt(OffsetDateTime.now())
//                    .build();

            return new FileUploadResponse(s3Url, key);

        } catch (Exception e) {
            log.error("Error uploading file for tenant {}: {}", tenantName, e.getMessage(), e);
            throw new RuntimeException("Could not upload file", e);
        }
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
}