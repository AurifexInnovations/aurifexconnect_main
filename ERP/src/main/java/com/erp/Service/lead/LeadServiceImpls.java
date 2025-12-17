package com.erp.Service.lead;


import com.erp.Dto.Request.LeadProductRequestDto;
import com.erp.Dto.Request.LeadRequest;

import com.erp.Dto.Request.LeadResponse;
import com.erp.Dto.Request.LeadServiceMapperDto;
import com.erp.Dto.Response.DropDown;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Mapper.crm.LeadMapper;
import com.erp.Model.*;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Lead.LeadProductMapperRepository;
import com.erp.Repository.Lead.LeadRepositorys;
import com.erp.Repository.User.UserRepository;
import com.erp.Repository.crm.LeadServiceMapperRepository;
import com.erp.Security.util.UserIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadServiceImpls implements LeadServices {

    private final LeadRepositorys leadRepository;
    private final LeadProductMapperRepository leadProductRepository;
    private final LeadMapper leadMapper;
    private final LeadServiceMapperRepository leadServiceMapperRepository;
    private final BranchRepository branchRepository;
    private final UserIdentity userIdentity;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Leads addOrUpdateLead(LeadRequest request) {
        log.info("Received request to add/update lead: {}", request);

        Leads lead;

        if (request.getId() != null) {
            lead = leadRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + request.getId()));

            log.info("Updating existing lead with ID: {}", lead.getId());
            lead.setLeadName(request.getLeadName());
            lead.setCompanyName(request.getCompanyName());
            lead.setEmail(request.getEmail());
            lead.setPhone(request.getPhone());
            lead.setSource(request.getSource());
            lead.setTypeOfLead(request.getTypeOfLead());
            lead.setLeadStatus(request.getLeadStatus());
            lead.setEngagementScore(request.getEngagementScore());
            lead.setRemarks(request.getRemarks());

            lead = leadRepository.save(lead);

            // clear old product mappings
            log.info("Deleting old product mappings for lead_id: {}", lead.getId());
            leadProductRepository.deleteByLeadId(lead.getId());
        } else {
            log.info("Creating new lead: {}", request.getLeadName());
            lead = Leads.builder()
                    .leadName(request.getLeadName())
                    .companyName(request.getCompanyName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .source(request.getSource())
                    .typeOfLead(request.getTypeOfLead())
                    .leadStatus(request.getLeadStatus() != null ? request.getLeadStatus() : "NEW")
                    .engagementScore(request.getEngagementScore() != null ? request.getEngagementScore() : 0)
                    .remarks(request.getRemarks())
                    .build();

            lead = leadRepository.save(lead);
        }

        if (request.getProducts() != null && !request.getProducts().isEmpty()) {
            log.info("Adding {} products for lead_id: {}", request.getProducts().size(), lead.getId());

            Leads finalLead = lead;
            List<LeadProductMapper> productMappers = request.getProducts().stream()
                    .map(p -> LeadProductMapper.builder()
                            .leadId(finalLead.getId())
                            .productId(p.getProductId())
                            .quantity(p.getQuantity())
                            .build())
                    .collect(Collectors.toList());

            leadProductRepository.saveAll(productMappers);
        }

        log.info("Lead successfully saved with ID: {}", lead.getId());
        return lead;
    }

    @Override
    public LeadResponse addService(LeadRequest request) {

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found !!"));

        Leads leads = new Leads();

        leads.setLeadName(request.getLeadName());
        leads.setCompanyName(request.getCompanyName());
        leads.setEmail(request.getEmail());
        leads.setPhone(request.getPhone());
        leads.setSource(request.getSource());
        leads.setTypeOfLead(request.getTypeOfLead());
        leads.setLeadStatus(request.getLeadStatus());
        leads.setEngagementScore(request.getEngagementScore());
        leads.setRemarks(request.getRemarks());
        leads.setBranch(branch);

        if (request.getTypeOfLead().equals("SERVICE")) {
            leads.setSqrt(request.getSqrt());
            leads.setServiceCategory(request.getServiceCategory());
        }

        Leads saved = leadRepository.save(leads);

        if (request.getTypeOfLead().equals("PRODUCT")) {
            List<LeadProductMapper> productMappers = new ArrayList<>();
            for (LeadProductRequestDto requestDto : request.getProducts()) {
                LeadProductMapper leadProductMapper = new LeadProductMapper();
                leadProductMapper.setLeadId(saved.getId());
                leadProductMapper.setProductId(requestDto.getProductId());
                leadProductMapper.setQuantity(requestDto.getQuantity());
                productMappers.add(leadProductMapper);
            }

            leadProductRepository.saveAll(productMappers);
        }
        if (request.getTypeOfLead().equals("SERVICE")) {
            List<LeadServiceMapper> list = new ArrayList<>();
            for (Long id : request.getServices()) {
                LeadServiceMapper leadServiceMapper = new LeadServiceMapper();
                leadServiceMapper.setServiceId(id);
                leadServiceMapper.setLeadId(saved.getId());
                list.add(leadServiceMapper);
            }

            leadServiceMapperRepository.saveAll(list);
        }

        Leads properSaved = leadRepository.save(saved);

        return toResponseDto(properSaved);
    }

    private LeadResponse toResponseDto(Leads leads) {
        LeadResponse leadResponse = leadMapper.toResponseDto(leads);

        // For setting a branch in response
        leadResponse.setBranchId(leads.getBranch().getBranchId());

        // for setting Products and services in response
        List<LeadProductMapper> leadMappers = leadProductRepository.findByLeadId(leads.getId());
        List<LeadProductRequestDto> leadProductRequestDtos = new ArrayList<>();
        for (LeadProductMapper leadProductMapper : leadMappers) {
            LeadProductRequestDto requestDto = new LeadProductRequestDto();
            requestDto.setProductId(leadProductMapper.getProductId());
            requestDto.setQuantity(leadProductMapper.getQuantity());
            leadProductRequestDtos.add(requestDto);
        }

        List<Long> services = leadServiceMapperRepository.findServiceIdsByLeadId(leads.getId());
        leadResponse.setServices(services == null || services.isEmpty() ? Collections.emptyList() : services);
        leadResponse.setProducts(leadProductRequestDtos);
        return leadResponse;
    }


    @Override
    public ResultDto<LeadResponse> getAllLeads() {
        List<LeadResponse> leadResponses = new ArrayList<>();
        for (Leads leads : leadRepository.findAll()) {
            leadResponses.add(toResponseDto(leads));
        }

        ResultDto<LeadResponse> leadResponseResultDto = new ResultDto<>();
        leadResponseResultDto.setResults(leadResponses);
        leadResponseResultDto.setCount(leadResponses.size());
        return leadResponseResultDto;
    }

    @Override
    public LeadResponse getById(long id) {
        Leads leads = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leads Not Found !!"));

        return toResponseDto(leads);
    }

    @Override
    public LeadResponse deleteById(long id) {
        Leads leads = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leads Not Found !!"));

        leadRepository.deleteById(id);

        return toResponseDto(leads);
    }

    @Override
    public LeadResponse updateStatus(LeadRequest leadRequest) {
        Leads leads = leadRepository.findById(leadRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Lead Not Found !!"));

        leads.setLeadStatus(leadRequest.getLeadStatus());

        if (leadRequest.getLeadStatus().equalsIgnoreCase("LOST")) {
            leads.setLostReason(leadRequest.getLostReason());
            leads.setRemarks(leadRequest.getRemarks());
        } else if (leadRequest.getLeadStatus().equalsIgnoreCase("CONVERTED")) {
            // Here, When Customer Converted
        } else {
            leads.setRemarks(leadRequest.getRemarks());
        }

        Leads updated = leadRepository.save(leads);
        return toResponseDto(updated);
    }

    @Override
    public ResultDto<LeadResponse> getAllBranchWise() {
        GenericUser genericUser = userIdentity.getCurrentUser();

        User user = userRepository.findById(genericUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found !!"));

        List<LeadResponse> leadResponseList = new ArrayList<>();
        for(Leads leads : leadRepository.findByBranch_BranchId(user.getBranch().getBranchId())){
            LeadResponse response = toResponseDto(leads);
            leadResponseList.add(response);
        }

        ResultDto<LeadResponse> resultDto = new ResultDto<>();
        resultDto.setResults(leadResponseList);
        resultDto.setCount(leadResponseList.size());
        return resultDto;
    }

    @Override
    public ResultDto<DropDown> getDropDown() {
        GenericUser genericUser = userIdentity.getCurrentUser();

        User user = userRepository.findById(genericUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found !!"));

        List<DropDown> dropDowns = new ArrayList<>();
        for(Leads leads : leadRepository.findByBranch_BranchId(user.getBranch().getBranchId())){
            DropDown response = new DropDown(leads.getId(), leads.getLeadName());
            dropDowns.add(response);
        }

        ResultDto<DropDown> resultDto = new ResultDto<>();
        resultDto.setResults(dropDowns);
        resultDto.setCount(dropDowns.size());
        return resultDto;
    }
}
