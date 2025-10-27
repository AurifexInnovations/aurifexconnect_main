package com.erp.Projection;

import com.erp.Model.Action;
import com.erp.Model.Module;

public interface RoleModuleActionProjection {

    Long getUserPermissionId();

    Long getRoleModulePermissionId();

    Long getRoleId();
    String getRoleName();

    Module getModule();
    Action getAction();

    interface ModuleView {
        Long getId();
        String getName();
    }

    interface ActionView {
        Long getId();
        String getName();
    }
}