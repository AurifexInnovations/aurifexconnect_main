package com.erp.Dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AdminResponse {

    private long id;

    private String name;

    private String email;

    private long contactNo;

    @JsonProperty("isActive")
    private boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long createdByRootUserId;

    private long lastUpdatedByRootUserId;
}
