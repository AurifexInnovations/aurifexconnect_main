package com.erp.Dto.Response;

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

    private boolean is_Active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long createdByRootUserId;

    private long lastUpdatedByRootUserId;
}
