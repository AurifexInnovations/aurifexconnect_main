package com.erp.Service.lead;


import com.erp.Dto.Request.LeadRequest;

import com.erp.Model.LeadProductMapper;
import com.erp.Model.Leads;
import com.erp.Repository.Lead.LeadProductMapperRepository;
import com.erp.Repository.Lead.LeadRepositorys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadServiceImpls implements LeadServices {

    private final LeadRepositorys leadRepository;
    private final LeadProductMapperRepository leadProductRepository;

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
}
