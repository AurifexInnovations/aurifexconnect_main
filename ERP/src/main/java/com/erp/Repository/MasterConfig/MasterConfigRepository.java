package com.erp.Repository.MasterConfig;

import com.erp.Model.MasterConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterConfigRepository extends JpaRepository<MasterConfig, Long> {
    MasterConfig getByServiceName(String configname);
}
