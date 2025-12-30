package com.erp.Service.Cutomer;

import com.erp.CustomRepository.CustomerDetailsCustomRepository;
import com.erp.CustomRepository.TaskTechnicianCustomRepository;
import com.erp.Dto.Request.CustomerDetailsRequestDto;
import com.erp.Dto.Request.CustomerMapperRequestDto;
import com.erp.Dto.Request.FilterRequest;

import com.erp.Dto.Request.LeadServiceMapperDto;
import com.erp.Dto.Response.CustomerResponse;
import com.erp.Dto.Response.CustomerResponseDtos;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.ResultDto;
import com.erp.Events.Invoice.EnhanceQuotation.QuotationAcceptedEvent;
import com.erp.Dto.Response.*;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.ResourceFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.CustomerMapper;
import com.erp.Model.*;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.EnhanceQuotation.EnhanceQuotationRepository;
import com.erp.Repository.Lead.LeadProductMapperRepository;
import com.erp.Repository.Lead.LeadRepositorys;
import com.erp.Repository.User.UserRepository;
import com.erp.Repository.costumer.CustomerDetailsMapperRepository;
import com.erp.Repository.costumer.CustomerDetailsRepository;

import com.erp.Repository.crm.CustomerServiceMapperRepository;
import com.erp.Repository.crm.LeadRepository;
import com.erp.Repository.crm.LeadServiceMapperRepository;
import com.erp.Security.util.UserIdentity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerDetailsServiceImpl implements com.erp.Service.Cutomer.CustomerDetailsService {

    private final CustomerDetailsRepository customerRepo;
    private final CustomerDetailsMapperRepository mapperRepo;
    private final CustomerDetailsCustomRepository customerDetailsCustomRepository;
    private final com.erp.Mapper.crm.CustomerMapper customerMapper;
    private final CustomerServiceMapperRepository customerServiceMapperRepository;
    private final LeadRepositorys leadRepositorys;
    private final UserRepository userRepository;
    private final UserIdentity userIdentity;
    private final BranchRepository branchRepository;

    private final LeadProductMapperRepository leadProductMapperRepository;
    private final LeadServiceMapperRepository leadServiceMapperRepository;
    private final EnhanceQuotationRepository enhanceQuotationRepository;


    private final TaskTechnicianCustomRepository taskTechnicianCustomRepository;

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

    @EventListener
    @Transactional
        public void handleQuotationAccepted(QuotationAcceptedEvent event){

        EnhanceQuotation quotation = event.getQuotation();
        Long leadId = quotation.getLeadId();

            Leads lead = leadRepositorys.findById(leadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + leadId));

            Leads leadsMail = leadRepositorys.findByEmail(lead.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + lead.getEmail()));

            if(leadsMail.getLeadStatus().equalsIgnoreCase("CONVERTED")){
                throw new ResourceFoundException("Customer Already Exists for this Email");
            }else {
                CustomerDetails customerDetails = convertLeadToCustomer(lead,quotation);
                customerRepo.save(customerDetails);

                lead.setLeadStatus("CONVERTED");
                lead.setConvertedCustomer(customerDetails);
                leadRepositorys.save(lead);

                quotation.setCustomerId(customerDetails.getId());
                enhanceQuotationRepository.save(quotation);

            }

        }

        private CustomerDetails convertLeadToCustomer(Leads leads,EnhanceQuotation quotation){

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerName(leads.getLeadName());
        customerDetails.setCompanyName(leads.getCompanyName());
        // add all lead quotation to converted customer
        customerDetails.setTotalQuotation(leads.getTotalQuotation());
        customerDetails.setServiceCategory(String.valueOf(leads.getServiceCategory()));
        customerDetails.setCustomerStatus("ACTIVE");
        customerDetails.setCustomerType(leads.getTypeOfLead());
        customerDetails.setPhone(leads.getPhone());
        customerDetails.setEmail(leads.getEmail());
        customerDetails.setAlternatePhone(quotation.getAlternatePhone());
        customerDetails.setCountry(quotation.getCountry());
        customerDetails.setBranch(quotation.getBranch());
        customerDetails.setCreatedAt(LocalDateTime.now());
        customerDetails.setJoinedDate(LocalDate.now());
        customerDetails.setLandmark(quotation.getLandmark());
        customerDetails.setLocationUrl(quotation.getLocationUrl());
        customerDetails.setPincode(quotation.getPincode());
        customerDetails.setState(quotation.getState());
        customerDetails.setUpdatedAt(quotation.getUpdatedAt());
       // customerDetails.setTags();
        customerDetails.setCity(quotation.getCity());
        customerDetails.setAddressLine1(quotation.getAddressLine1());
        customerDetails.setAddressLine2(quotation.getAddressLine2());


        if("SERVICE".equalsIgnoreCase(quotation.getQuotationType())){
            BigDecimal quotationSqft = quotation.getSqft();
            customerDetails.setSqrt(quotationSqft.doubleValue());
        }
           CustomerDetails saveCustomer = customerRepo.save(customerDetails);

            String leadType = leads.getTypeOfLead();
            if ("PRODUCT".equalsIgnoreCase(leadType)) {
                handleProductMapping(leads.getId(), saveCustomer.getId() /* inject repos if needed */);
            } else if ("SERVICE".equalsIgnoreCase(leadType)) {
                handleServiceMapping(leads.getId(), saveCustomer.getId() /* inject repos if needed */);
            }

      return customerRepo.save(saveCustomer);

        }

    private void handleProductMapping(Long leadId, Long customerId) {
        // Fetch lead products directly via repo
        List<LeadProductMapper> leadProducts = leadProductMapperRepository.findByLeadId(leadId);
        if (leadProducts != null && !leadProducts.isEmpty()) {
            List<CustomerDetailsMapper> customerProducts = new ArrayList<>();
            for (LeadProductMapper leadProduct : leadProducts) {
                CustomerDetailsMapper mapper = new CustomerDetailsMapper();
                mapper.setCustomerId(customerId);
                mapper.setProductId(leadProduct.getProductId());
                mapper.setQuantity(leadProduct.getQuantity());  // Map quantity
                customerProducts.add(mapper);
            }
            mapperRepo.saveAll(customerProducts);
            log.debug("Migrated {} products for customer {}", customerProducts.size(), customerId);
        }
    }

    // Extracted: Handle Service Mapping (Direct Repo)
    private void handleServiceMapping(Long leadId, Long customerId) {
        // Fetch lead services directly via repo
        List<LeadServiceMapper> leadServices = leadServiceMapperRepository.findByLeadId(leadId);
        if (leadServices != null && !leadServices.isEmpty()) {
            List<CustomerServiceMapper> customerServices = new ArrayList<>();
            for (LeadServiceMapper leadService : leadServices) {
                CustomerServiceMapper mapper = new CustomerServiceMapper();
                mapper.setCustomerId(customerId);
                mapper.setServiceId(leadService.getServiceId());
                customerServices.add(mapper);
            }
            customerServiceMapperRepository.saveAll(customerServices);
            log.debug("Migrated {} services for customer {}", customerServices.size(), customerId);
        }
    }

    @Override
    public ResultDto<CustomerResponse> getFilteredCustomers(FilterRequest filterRequest) {
        log.info("[CustomerServiceImpl] getFilteredCustomers() called with request: {}", filterRequest);

        try {
            ResultDto<CustomerResponse> result = customerDetailsCustomRepository.getFilteredCustomers(filterRequest);

            log.info("[CustomerServiceImpl] getFilteredCustomers() successful. Total Records: {}", result.getCount());

            return result;

        } catch (Exception ex) {
            log.error("[CustomerServiceImpl] Error in getFilteredCustomers(): {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch customer records. Please try again later.", ex);
        }
    }

    @Override
    public CustomerResponse addCustomer(CustomerDetailsRequestDto dto) {

        // Step 1: Check if any lead exists with same email
        Optional<Leads> existingLeadOpt = leadRepositorys.findByEmail(dto.getEmail());
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found !!"));

        Leads lead = null;

        if (existingLeadOpt.isPresent()) {
            lead = existingLeadOpt.get();

            // A. If lead is already converted → customer exists → throw error
            if (lead.getLeadStatus().equalsIgnoreCase("CONVERTED")) {
                throw new ResourceFoundException("Customer Already Exists for this Email");
            }
        }

        // Step 2: Create Customer
        CustomerDetails entity = new CustomerDetails();

        entity.setCustomerName(dto.getCustomerName());
        entity.setCompanyName(dto.getCompanyName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddressLine1(dto.getAddressLine1());
        entity.setAddressLine2(dto.getAddressLine2());
        entity.setLandmark(dto.getLandmark());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setCountry(dto.getCountry());
        entity.setPincode(dto.getPincode());
        entity.setTags(dto.getTags());
        entity.setCustomerStatus(dto.getCustomerStatus());
        entity.setJoinedDate(LocalDate.now());
        entity.setAlternatePhone(dto.getAlternatePhone());
        entity.setLocationUrl(dto.getLocationUrl());
        entity.setCustomerType(dto.getCustomerType());
        entity.setTotalQuotation(dto.getTotalQuotation());
        entity.setTotalSalesOrder(dto.getTotalSalesOrder());
        entity.setTotalInvoices(dto.getTotalInvoices());
        entity.setBranch(branch);

        if ("SERVICE".equalsIgnoreCase(dto.getCustomerType())) {
            entity.setServiceCategory(dto.getServiceCategory());
            entity.setSqrt(dto.getSqrt());
        }

        CustomerDetails saved = customerRepo.save(entity);

        // Step 3: If a lead existed → Convert Lead
        if (lead != null) {
            lead.setLeadStatus("CONVERTED");
            lead.setConvertedCustomer(saved);
            leadRepositorys.save(lead);
        }

        // Step 4: PRODUCT mapping
        if ("PRODUCT".equalsIgnoreCase(dto.getCustomerType()) && dto.getProducts() != null) {

            List<CustomerDetailsMapper> list = new ArrayList<>();
            for (CustomerMapperRequestDto p : dto.getProducts()) {
                CustomerDetailsMapper m = new CustomerDetailsMapper();
                m.setCustomerId(saved.getId());
                m.setQuantity(p.getQuantity());
                m.setProductId(p.getProductId());
                list.add(m);
            }
            mapperRepo.saveAll(list);
        }

        // Step 5: SERVICE mapping
        if ("SERVICE".equalsIgnoreCase(dto.getCustomerType()) && dto.getServices() != null) {

            List<CustomerServiceMapper> list = new ArrayList<>();
            for (Long id : dto.getServices()) {
                CustomerServiceMapper m = new CustomerServiceMapper();
                m.setCustomerId(saved.getId());
                m.setServiceId(id);
                list.add(m);
            }
            customerServiceMapperRepository.saveAll(list);
        }

        return toResponseDto(saved);
    }


    private CustomerResponse toResponseDto(CustomerDetails customerDetails) {

        CustomerResponse customerResponse = customerMapper.toCustomerResponseDto(customerDetails);

        List<Long> services = customerServiceMapperRepository.findServiceIdsByCustomerId(customerDetails.getId());
        customerResponse.setServices(services);
        customerResponse.setBranchId(customerDetails.getBranch().getBranchId());

        List<CustomerDetailsMapper> customerDetailsMappers = mapperRepo.findByCustomerId(customerDetails.getId());
        List<CustomerMapperRequestDto> list = new ArrayList<>();
        for (CustomerDetailsMapper customerDetailsMapper : customerDetailsMappers) {
            CustomerMapperRequestDto customerMapperRequestDto = new CustomerMapperRequestDto();
            customerMapperRequestDto.setQuantity(customerDetailsMapper.getQuantity());
            customerMapperRequestDto.setProductId(customerDetailsMapper.getProductId());
            list.add(customerMapperRequestDto);
        }
        customerResponse.setProducts(list);

        return customerResponse;
    }

    @Override
    public ResultDto<CustomerResponse> getAll() {
        List<CustomerResponse> list = new ArrayList<>();
        for (CustomerDetails customerDetails : customerRepo.findAll()) {

            list.add(toResponseDto(customerDetails));
        }

        ResultDto<CustomerResponse> resultDto = new ResultDto<>();
        resultDto.setCount(list.size());
        resultDto.setResults(list);

        return resultDto;
    }

    @Override
    public CustomerResponse getById(long id) {
        CustomerDetails customerDetails = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found !!"));

        return toResponseDto(customerDetails);
    }


    @Override
    public CustomerResponse deleteById(long id) {
        CustomerDetails customerDetails = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found !!"));

        customerRepo.delete(customerDetails);

        return toResponseDto(customerDetails);
    }


    @Override
    public ResultDto<CustomerResponse> getByBranchWise() {
        GenericUser genericUser = userIdentity.getCurrentUser();
        User user = userRepository.findById(genericUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found !!"));


        List<CustomerResponse> customerResponseList = new ArrayList<>();
        for(CustomerDetails customerDetails : customerRepo.findByBranch_BranchId(user.getBranch().getBranchId())){
            customerResponseList.add(toResponseDto(customerDetails));
        }

        ResultDto<CustomerResponse> resultDto = new ResultDto<>();
        resultDto.setCount(customerResponseList.size());
        resultDto.setResults(customerResponseList);
        return resultDto;
    }


    @Override
    public ResultDto<DropDown> getDropdown() {

        GenericUser genericUser = userIdentity.getCurrentUser();
        User user = userRepository.findById(genericUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found !!"));

        List<DropDown> list = new ArrayList<>();
        for (CustomerDetails customer : customerRepo.findByBranch_BranchId(user.getBranch().getBranchId())) {
            DropDown dropDown = new DropDown(customer.getId(), customer.getCustomerName());
            list.add(dropDown);
        }

        ResultDto<DropDown> resultDto = new ResultDto<>();
        resultDto.setCount(list.size());
        resultDto.setResults(list);
        return  resultDto;
    }

    @Override
    public ResultDto<TechnicianResponseDTO> findCustomerWithTask(FilterRequest filterRequest) {
        ResultDto<TechnicianResponseDTO> result = taskTechnicianCustomRepository.searchTasks(filterRequest);
        return result;
    }
}
