package com.erp.Service.Admin;

import com.erp.Dto.Request.AdminRequest;
import com.erp.Dto.Request.CommanParam;
import com.erp.Dto.Response.AdminResponse;
import com.erp.Exception.Admin.AdminAlreadyExistsException;
import com.erp.Exception.Admin.AdminNotFoundException;
import com.erp.Mapper.Admin.AdminMapper;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Model.Admin;
import com.erp.Model.RootUser;
import com.erp.Multitenancy.TenantContextHolder;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Rootuser.RootUserRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.Schema.SchemaManagementService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserIdentity userIdentity;
    private final AdminUserRepository adminRepository;
    private final AdminMapper adminMapper;
    private final RootUserRepository rootUserRepository;
    private final SchemaManagementService schemaManagementService;
    private final MetaAdminRepository metaAdminRepository;
    private final AdminPersistenceService adminPersistenceService;

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
        return adminMapper.mapToAdminResponse(admin);
    }


    @Override
    public List<AdminResponse> getListOfAdmins() {
        List<Admin> admins = adminRepository.findByIsActiveTrue();
        return adminMapper.mapToListOfAdminResponse(admins);
    }

    @Override
    public AdminResponse updateAdminById(AdminRequest adminRequest) {
        RootUser currentUser = (RootUser) userIdentity.getCurrentUser();
        Admin admin = adminRepository.findById(adminRequest.getId())
                .orElseThrow(() -> new AdminNotFoundException("Invalid ID: " + adminRequest.getId() + " ,admin not found !"));
        adminMapper.mapToAdminEntity(adminRequest, admin);
        admin.setLastUpdatedByRootUserId(currentUser.getId());
        rootUserRepository.save(currentUser);
        adminRepository.save(admin);
        return adminMapper.mapToAdminResponse(admin);
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
        return adminMapper.mapToAdminResponse(admin);
    }

    @Override
    public List<AdminResponse> findAdminByIdOrName(CommanParam commonParam) {
        List<Admin> admins = Collections.singletonList(adminRepository.findByIdOrNameAndIsActiveTrue(commonParam.getId(), commonParam.getName())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found !!")));
        return adminMapper.mapToListOfAdminResponse(admins);
    }
}