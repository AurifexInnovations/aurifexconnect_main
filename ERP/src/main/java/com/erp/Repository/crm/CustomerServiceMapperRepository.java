package com.erp.Repository.crm;

import com.erp.Model.CustomerServiceMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerServiceMapperRepository extends JpaRepository<CustomerServiceMapper, Long> {

    List<CustomerServiceMapper> findByCustomerId(Long customerId);

    void deleteByCustomerId(Long customerId);

    @Query("SELECT c.serviceId FROM CustomerServiceMapper c WHERE c.customerId = :customerId")
    List<Long> findServiceIdsByCustomerId(@Param("customerId") Long id);
}
