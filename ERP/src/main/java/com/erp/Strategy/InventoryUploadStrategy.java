package com.erp.Strategy;

import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.GenericUser;
import com.erp.Repository.Inventory.InventoryRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.InventoryService.InventoryService;
import com.erp.Validator.FileTypeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static com.erp.constants.FileUploadConstants.INVENTORY_CATEGORIES;
import static com.erp.constants.FileUploadConstants.TASK_CATEGORIES;

@Component
@RequiredArgsConstructor
public class InventoryUploadStrategy implements FileUploadStrategy {

    // Base template path (placeholders will be replaced dynamically)
    private final String basePathTemplate = "uploads/inventory/{iId}/";

    @Qualifier("imageValidator")
    private final FileTypeValidator imageValidator;

    private final UserIdentity userIdentity;

    private final InventoryRepository inventoryRepository; // ✅ No InventoryService

    @Override
    public boolean supports(String category) {
        return INVENTORY_CATEGORIES.contains(category);
    }

    @Override
    public boolean validateId(Long inventoryId) {
        return inventoryRepository.existsById(inventoryId); // ✅ No cycle
    }


    @Override
    public List<String> uploadFiles(int seq, Long inventoryId, String category, MultipartFile[] files) {
        GenericUser user = userIdentity.getCurrentUser();

        boolean isValideTask = validateId(inventoryId);

        if (!isValideTask) {
            throw new ResourceNotFoundException("Invalid inventory provided: " + inventoryId);
        }

        List<String> filePaths = new ArrayList<>();

        try {
            // Replace placeholders dynamically
            String resolvedPath = basePathTemplate
                    .replace("{iId}", String.valueOf(inventoryId));

            Path uploadDir = Paths.get(resolvedPath);

            // ✅ Create directory if it doesn’t exist
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Upload each file
            for (MultipartFile file : files) {
                if (!imageValidator.isValid(file)) {
                    throw new ResourceNotFoundException("Invalid file type: " + file.getOriginalFilename());
                }

                String extension = getExtension(file.getOriginalFilename());
                String fileName = "file_" + (++seq) + "." + extension;

                Path target = uploadDir.resolve(fileName);

                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
                filePaths.add(target.toAbsolutePath().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }

        return filePaths;
    }


    private String getExtension(String filename) {
        return filename != null && filename.contains(".")
                ? filename.substring(filename.lastIndexOf(".") + 1)
                : "";
    }

}
