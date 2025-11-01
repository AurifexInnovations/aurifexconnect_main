package com.erp.Projection;

import com.erp.Model.Action;
import com.erp.Model.Module;

public interface RoleModuleActionProjection {
    Long getUserPermissionId();
    Long getRoleModulePermissionId();
    Long getRoleId();
    String getRoleName();
    Long getModuleId();
    String getModuleName();
    Long getActionId();
    String getActionName();
    String getDescription();
}

