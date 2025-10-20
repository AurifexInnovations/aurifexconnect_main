package com.erp.Dto.Response;

import com.erp.Enum.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountGroupResponse {

    private Long groupId;
    private String groupName;
    private String groupCode;
    private GroupType groupType;
    private String description;
    private Boolean isActive;
    private Integer sortOrder;
    private LocalDateTime createdDate;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private List<AccountSubGroupResponse> accountSubGroups;
    private Integer totalLedgers;
    private Integer totalSubGroups;

    public AccountGroupResponse(Long groupId, String groupName, String groupCode, GroupType groupType, 
                               String description, Boolean isActive, Integer sortOrder) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCode = groupCode;
        this.groupType = groupType;
        this.description = description;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
    }
}
