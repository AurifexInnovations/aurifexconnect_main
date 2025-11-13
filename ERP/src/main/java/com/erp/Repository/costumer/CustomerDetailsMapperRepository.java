package com.erp.Repository.costumer;

import com.erp.Model.CustomerDetailsMapper;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerDetailsMapperRepository extends JpaRepository<CustomerDetailsMapper, Long> {
    List<CustomerDetailsMapper> findByCustomerId(Long customerId);
    void deleteByCustomerId(Long customerId);
}
