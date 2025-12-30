package com.erp.Service.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Response.AdminResponse;
import com.erp.Dto.Response.AdminUpdateRequest;
import com.erp.Dto.Response.FileUploadResponse;
import com.erp.Exception.Admin.AdminAlreadyExistsException;
import com.erp.Exception.Admin.AdminNotFoundException;
import com.erp.Mapper.Admin.AdminMapper;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Model.Admin;
import com.erp.Model.FileInfoDto;
import com.erp.Model.GenericUser;
import com.erp.Model.RootUser;
import com.erp.Multitenancy.TenantContextHolder;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Rootuser.RootUserRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.Schema.SchemaManagementService;
import com.erp.Utility.inerfaces.S3StorageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final UserIdentity userIdentity;
    private final AdminUserRepository adminRepository;
    private final AdminMapper adminMapper;
    private final RootUserRepository rootUserRepository;
    private final SchemaManagementService schemaManagementService;
    private final MetaAdminRepository metaAdminRepository;
    private final AdminPersistenceService adminPersistenceService;
    private final S3StorageService s3StorageService;
    private final S3Presigner s3Presigner;

    private final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public AdminResponse createAdmin(AdminRequest adminRequest) {
        logger.info("Creating admin for email: {}", adminRequest.getEmail());

        String sanitizedEmail = adminRequest.getEmail().replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
        if (sanitizedEmail.length() > 63) {
            throw new IllegalArgumentException("Schema name too long");
        }
        long tenantCount;
        try (var context = new TenantContextHolder("public")) {
            tenantCount = metaAdminRepository.count() + 1;
        }
        String schemaName = "tenant_" + tenantCount + "_" + sanitizedEmail;
        logger.info("Generated schema: {}", schemaName);

        try (var context = new TenantContextHolder("public")) {
            if (metaAdminRepository.existsByAdminEmail(adminRequest.getEmail())) {
                throw new AdminAlreadyExistsException("Admin with email already exists: " + adminRequest.getEmail());
            }
        }

        schemaManagementService.createTenantSchema(schemaName, adminRequest.getEmail());
        logger.info("Tenant schema {} created successfully", schemaName);

        Admin admin = adminPersistenceService.saveAdminInSchema(adminRequest, schemaName);
        return toResponse(admin);
    }


    @Override
    public List<AdminResponse> getListOfAdmins() {
        List<Admin> admins = adminRepository.findByIsActiveTrue();
        List<AdminResponse> list = new ArrayList<>();
        for(Admin admin : admins){
            list.add(toResponse(admin));
        }
        return list;
    }

    @Override
    public AdminResponse updateAdminById(AdminUpdateRequest adminRequest, List<FileInfoDto> files) {
        GenericUser genericUser = userIdentity.getCurrentUser();

        Admin admin = adminRepository.findByEmail(genericUser.getEmail())
                        .orElseThrow(() -> new AdminNotFoundException("Admin Not Found !!"));

        admin.setName(adminRequest.getName());
        admin.setContactNo(adminRequest.getContactNo());

        if (files != null && files.size() > 0) {
            List<FileUploadResponse> fileUploadResponses =
                    s3StorageService.uploadFile(files, "admin/profile");

            if (!fileUploadResponses.isEmpty()) {
                admin.setDocumentUrl(fileUploadResponses.get(0).getS3Key());
            }
        }

        Admin saved = adminRepository.save(admin);
        return toResponse(saved);
    }

    @Override
    public AdminResponse deleteAdminById(CommanParam commanParam) {
        RootUser currentUser = (RootUser) userIdentity.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("No authenticated user found");
        }
        Admin admin = adminRepository.findById(commanParam.getId())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with this id: " + commanParam.getId()));
        admin.setActive(false);
        adminRepository.save(admin);
        return toResponse(admin);
    }

    @Override
    public AdminResponse findAdminById() {

        String email = userIdentity.getCurrentUserEmail();
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminNotFoundException("Admin Not Found!!"));

        return toResponse(admin);
    }

    private String generatePresignedUrl(String s3Key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(p -> p
                        .getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10)));

        return presignedRequest.url().toString();
    }

    private AdminResponse toResponse(Admin admin){
        AdminResponse adminResponse = adminMapper.mapToAdminResponse(admin);
        if(admin.getDocumentUrl() != null){
            adminResponse.setDocumentUrl(generatePresignedUrl(admin.getDocumentUrl()));
        }
        return adminResponse;
    }
}