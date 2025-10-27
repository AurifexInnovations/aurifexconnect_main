package com.erp.Dto.Response;

import com.erp.Dto.Request.ActionDto;
import com.erp.Dto.Request.ModuleDto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleModleActionPermisisonResponse {

    private long roleModulePermissionId;
    private Long userPermissonId;
    private RoleResponse role;
    private ModuleDto module;
    private ActionDto action;

}
