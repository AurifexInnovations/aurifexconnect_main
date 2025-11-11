package com.erp.Service.Cutomer;

import com.erp.Dto.Request.CustomerDetailsRequestDto;
import com.erp.Dto.Response.CustomerResponseDtos;
import com.erp.Mapper.CustomerMapper;
import com.erp.Model.CustomerDetails;
import com.erp.Model.CustomerDetailsMapper;
import com.erp.Repository.costumer.CustomerDetailsMapperRepository;
import com.erp.Repository.costumer.CustomerDetailsRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerDetailsServiceImpl implements com.erp.Service.Cutomer.CustomerDetailsService {

    @Autowired
    private CustomerDetailsRepository customerRepo;

    @Autowired
    private CustomerDetailsMapperRepository mapperRepo;

    @Override
    @Transactional
    public CustomerResponseDtos addOrUpdateCustomer(CustomerDetailsRequestDto request) {
        log.info("Starting addOrUpdateCustomer: {}", request.getCustomerName());

        CustomerDetails customer = CustomerMapper.toEntity(request);
        customer = customerRepo.saveAndFlush(customer);
        log.info("Customer saved successfully with ID: {}", customer.getId());

        // Remove old mappings if updating
        mapperRepo.deleteByCustomerId(customer.getId());

        if (request.getProducts() != null && !request.getProducts().isEmpty()) {
            CustomerDetails finalCustomer = customer;
            List<CustomerDetailsMapper> productMappings = request.getProducts().stream()
                    .map(p -> CustomerDetailsMapper.builder()
                            .customerId(finalCustomer.getId())
                            .productId(p.getProductId())
                            .quantity(p.getQuantity())
                            .build())
                    .collect(Collectors.toList());

            mapperRepo.saveAll(productMappings);
            log.info("Saved {} product mappings for customer ID {}", productMappings.size(), customer.getId());
        }

        return CustomerResponseDtos.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .companyName(customer.getCompanyName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .city(customer.getCity())
                .country(customer.getCountry())
                .tags(customer.getTags())
                .customerStatus(customer.getCustomerStatus())
                .joinedDate(customer.getJoinedDate())
                .products(request.getProducts())
                .build();
    }
}
