package com.erp.Service.crm;

import com.erp.Dto.Request.CustomerRequestDto;
import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Mapper.crm.CustomerMapper;
import com.erp.Model.Customer;
import com.erp.Repository.crm.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public Page<CustomerResponseDto> findByCriteria(String name, String email, Pageable pageable) {
        return null;
    }

    public CustomerResponseDto create(CustomerRequestDto dto) {
        Customer entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public CustomerResponseDto update(Long id, CustomerRequestDto dto) {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public CustomerResponseDto findById(Long id) {
        Customer entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return mapper.toDto(entity);
    }

    public List<CustomerResponseDto> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }


}
