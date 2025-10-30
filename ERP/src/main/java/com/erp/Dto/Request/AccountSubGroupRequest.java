package com.erp.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSubGroupRequest {

    private Long subgroupId;

    @NotBlank(message = "Subgroup name is required")
    @Size(max = 255, message = "Subgroup name cannot exceed 255 characters")
    private String subgroupName;

    @NotBlank(message = "Subgroup code is required")
    @Pattern(regexp = "^[A-Z0-9]{2,15}$", message = "Subgroup code must be 2-15 uppercase alphanumeric characters")
    private String subgroupCode;

    @NotNull(message = "Account group ID is required")
    private Long groupId;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Builder.Default
    private Boolean isActive = true;

    private Integer sortOrder;
}
