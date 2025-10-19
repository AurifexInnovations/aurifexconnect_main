package com.erp.Mapper.MasterConfig;

import com.erp.Dto.MasterConfigDTO;
import com.erp.Model.MasterConfig;

public class MasterConfigMapper {

    public static MasterConfigDTO toDTO(MasterConfig entity) {
        if (entity == null) {
            return null;
        }

        MasterConfigDTO dto = new MasterConfigDTO();
        dto.setMasterConfigId(entity.getMasterConfigId());
        dto.setServiceName(entity.getServiceName());
        dto.setJsonData(entity.getJsonData());
        dto.setUserid(entity.getUserid());

        return dto;
    }


}
