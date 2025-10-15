package com.erp.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MasterConfigDTO {

    private Long masterConfigId;
    private String serviceName;
    private String jsonData;
    private String userid;
}
