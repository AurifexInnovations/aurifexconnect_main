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
        log.info("findByCriteria called with name={}, email={}, pageable={}", name, email, pageable);
        return null;
    }

    public CustomerResponseDto create(CustomerRequestDto dto) {
        log.info("create called with dto={}", dto);
        Customer entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        log.info("Customer created with id={}", entity.getId());
        return mapper.toDto(entity);
    }

    public CustomerResponseDto findById(Long id) {
        log.info("findById called with id={}", id);
        Customer entity = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer not found for id={}", id);
                    return new EntityNotFoundException("Customer not found");
                });
        log.info("Customer found id={}", id);
        return mapper.toDto(entity);
    }

    public List<CustomerResponseDto> findAll() {
        log.info("findAll called");
        List<CustomerResponseDto> result = mapper.toDtoList(repository.findAll());
        log.info("findAll returned {} customers", result.size());
        return result;
    }

    public void deleteById(Long id) {
        log.info("deleteById called with id={}", id);
        repository.deleteById(id);
        log.info("deleteById completed for id={}", id);
    }

    public void deleteAll() {
        log.info("deleteAll called");
        repository.deleteAll();
        log.info("deleteAll completed");
    }
}