package com.erp.Service.SubscriptionService;

import com.erp.Dto.MasterConfigDTO;
import com.erp.Dto.Response.UserSubscriptionResponse;

public interface IMasterConfigService {
    MasterConfigDTO fetchMasterConfigDataByConfigName(String configname);
}
