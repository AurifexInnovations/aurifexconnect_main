package com.erp.Service.SubscriptionService;

import com.erp.Dto.MasterConfigDTO;
import com.erp.Mapper.MasterConfig.MasterConfigMapper;
import com.erp.Model.MasterConfig;
import com.erp.Repository.MasterConfig.MasterConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterConfigServiceImpl implements IMasterConfigService {

    @Autowired
    MasterConfigRepository masterConfigRepository;

    @Override
    public MasterConfigDTO fetchMasterConfigDataByConfigName(String serviceName) {

        try {
            MasterConfig m = masterConfigRepository.getByServiceName(serviceName);

            MasterConfigMapper m1 = new MasterConfigMapper();
            if (m != null) {
                return m1.toDTO(m);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
