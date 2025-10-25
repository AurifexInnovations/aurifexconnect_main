package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PermissionRequest {
    private Long moduleId;
    private Set<Long> actionId;
}
