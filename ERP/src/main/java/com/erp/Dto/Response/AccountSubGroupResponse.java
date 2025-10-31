package com.erp.Dto.Response;

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
public class AccountSubGroupResponse {

    private Long subgroupId;
    private String subgroupName;
    private String subgroupCode;
    private Long groupId;
    private String groupName;
    private String groupCode;
    private String description;
    private Boolean isActive;
    private Integer sortOrder;
    private LocalDateTime createdDate;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private List<LedgerResponse> ledgers;
    private Integer totalLedgers;

    public AccountSubGroupResponse(Long subgroupId, String subgroupName, String subgroupCode, 
                                  Long groupId, String groupName, String groupCode,
                                  String description, Boolean isActive, Integer sortOrder) {
        this.subgroupId = subgroupId;
        this.subgroupName = subgroupName;
        this.subgroupCode = subgroupCode;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCode = groupCode;
        this.description = description;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
    }
}
