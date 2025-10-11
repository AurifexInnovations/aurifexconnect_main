package com.erp.Repository.Service;

import com.erp.Enum.ServiceStatus;
import com.erp.Model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceRepository extends JpaRepository<Service,Long> {

    List<Service> findByServiceIdOrServiceName(long serviceId,String name);

    List<Service> findByServiceStatus(ServiceStatus serviceStatus);

    List<Service> findServiceByCategories(String categories);

    List<Service> findAll();



}
