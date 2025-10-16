package com.erp.specification.crm;

import com.erp.Dto.Response.CustomerResponseDto;
import com.erp.Mapper.crm.CustomerMapper;
import com.erp.Model.Customer;
import com.erp.Repository.crm.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerSpecifications {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public static Specification<Customer> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Customer> hasEmail(String email) {
        return (root, query, cb) ->
                email == null ? null : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public Page<CustomerResponseDto> findByCriteria(String name, String email, Pageable pageable) {
        Specification<Customer> spec = Specification.where(CustomerSpecifications.hasName(name))
                .and(CustomerSpecifications.hasEmail(email));
        Page<Customer> page = repository.findAll(spec, pageable);
        return page.map(mapper::toDto);
    }

}
