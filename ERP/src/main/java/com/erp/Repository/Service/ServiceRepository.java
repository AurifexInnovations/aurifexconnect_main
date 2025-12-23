package com.erp.Repository.Service;

import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import com.erp.Model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Long> {

    List<Service> findByServiceIdOrServiceName(long serviceId,String name);

    List<Service> findByServiceStatus(ServiceStatus serviceStatus);

    List<Service> findByServiceCategory(ServiceCategory serviceCategory);

    List<Service> findAll();

    List<Service> findByBranch_BranchIdAndServiceStatus(long branchId, ServiceStatus serviceStatus);

    List<Service> findByBranch_BranchId(long branchId);

    Optional<Service> findByServiceIdAndServiceStatus(Long serviceId, ServiceStatus serviceStatus);
}
