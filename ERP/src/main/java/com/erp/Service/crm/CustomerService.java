package com.erp.Service.crm;

import com.erp.Dto.Request.CustomerRequestDto;
import com.erp.Dto.Response.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Page<CustomerResponseDto> findByCriteria(String name, String email, Pageable pageable);

    CustomerResponseDto create(CustomerRequestDto dto);

    CustomerResponseDto findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    List<CustomerResponseDto> findAll();

}
