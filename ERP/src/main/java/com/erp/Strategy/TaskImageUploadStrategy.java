package com.erp.Strategy;

import com.erp.Exception.ResourceNotFoundException;
import com.erp.Model.GenericUser;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.TaskService.TaskService;
import com.erp.Validator.FileTypeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static com.erp.constants.FileUploadConstants.CATEGORIES;

@Component
@RequiredArgsConstructor
public class TaskImageUploadStrategy implements FileUploadStrategy {

    // Base template path (placeholders will be replaced dynamically)
    private final String basePathTemplate = "uploads/technitian/{tId}/task/{taskId}/category/{cName}";

    @Qualifier("imageValidator")
    private final FileTypeValidator imageValidator;

    private final TaskService taskService;
    private final UserIdentity userIdentity;

    @Override
    public boolean supports(String category) {
        return CATEGORIES.contains(category);
    }

    @Override
    public boolean validateId(Long taskId) {
        return taskService.getTask(taskId); // should return true/false
    }

    @Override
    public List<String> uploadFiles(int seq, Long taskId, String category, MultipartFile[] files) {
        GenericUser user = userIdentity.getCurrentUser();
        Long tId = user.getId();

        boolean isValideTask = validateId(taskId);
        if (!isValideTask) {
            throw new ResourceNotFoundException("Invalid taskId provided: " + taskId);
        }

        List<String> filePaths = new ArrayList<>();

        try {
            // Replace placeholders dynamically
            String resolvedPath = basePathTemplate
                    .replace("{tId}", String.valueOf(tId))
                    .replace("{taskId}", String.valueOf(taskId))
                    .replace("{cName}", category);

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
