package com.erp.Dto.Request;

import com.erp.Enum.GroupType;
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
public class AccountGroupRequest {

    private Long groupId;

    @NotBlank(message = "Group name is required")
    @Size(max = 255, message = "Group name cannot exceed 255 characters")
    private String groupName;

    @NotBlank(message = "Group code is required")
    @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "Group code must be 2-10 uppercase alphanumeric characters")
    private String groupCode;

    @NotNull(message = "Group type is required")
    private GroupType groupType;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Builder.Default
    private Boolean isActive = true;

    private Integer sortOrder;
}
