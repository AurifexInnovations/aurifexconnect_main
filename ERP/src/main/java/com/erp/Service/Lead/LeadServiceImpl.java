package com.erp.Service.Lead;

import com.erp.Dto.Request.LeadRequest;
import com.erp.Dto.Request.Param;
import com.erp.Dto.Response.LeadMonthlyChartResponse;
import com.erp.Dto.Response.LeadResponse;
import com.erp.Dto.Response.LeadStatusSummaryResponse;
import com.erp.Enum.LeadStatus;
import com.erp.Mapper.Contact.ContactMapper;
import com.erp.Mapper.Lead.LeadMapper;
import com.erp.Model.Contact;
import com.erp.Model.Lead;
import com.erp.Repository.Contact.ContactRepository;
import com.erp.Repository.Lead.LeadRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepo;
    private final ContactRepository contactRepo;
    private final LeadMapper leadMapper;
    private final ContactMapper contactMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public LeadResponse create(LeadRequest request) {
        Lead lead = leadMapper.mapToEntity(request);
        lead.setStatus(LeadStatus.NEW);
        return leadMapper.mapToResponse(leadRepo.save(lead));
    }

    @Override
    public LeadResponse update(Long id, LeadRequest request) {
        Lead lead = leadRepo.findById(id).orElseThrow(() -> new RuntimeException("Lead not found"));
        leadMapper.updateEntityFromRequest(request, lead);
        return leadMapper.mapToResponse(leadRepo.save(lead));
    }

    @Override
    public List<LeadResponse> getAll() {
        return leadMapper.mapToResponseList(leadRepo.findAll());
    }

    @Override
    public LeadResponse getById(Long id) {
        return leadMapper.mapToResponse(leadRepo.findById(id).orElseThrow(() -> new RuntimeException("Lead not found")));
    }

    @Override
    public void delete(Long id) {
        leadRepo.deleteById(id);
    }

    @Override
    @Transactional
    public LeadResponse convertToContact(Long id) {
        Lead lead = leadRepo.findById(id).orElseThrow(() -> new RuntimeException("Lead not found"));
        Contact contact = contactMapper.mapFromLead(lead);
        contactRepo.findByEmail(contact.getEmail()).ifPresentOrElse(
                c -> {},
                () -> contactRepo.save(contact)
        );
        lead.setStatus(LeadStatus.CONVERTED);
        leadRepo.save(lead);
        return leadMapper.mapToResponse(lead);
    }

    @Override
    public List<LeadMonthlyChartResponse> getMonthlyLeadsChart(Param param) {
        String sql = """
            SELECT DATE(l.createdAt) AS date, COUNT(*) AS count
            FROM Lead l
            WHERE MONTH(l.createdAt) = :month AND YEAR(l.createdAt) = :year
            GROUP BY DATE(l.createdAt)
            ORDER BY DATE(l.createdAt)
        """;
        Query query = entityManager.createQuery(sql);
        LocalDate now = LocalDate.now();
        query.setParameter("month", now.getMonthValue());
        query.setParameter("year", now.getYear());

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(r -> {
                    LeadMonthlyChartResponse dto = new LeadMonthlyChartResponse();
                    dto.setDate(r[0].toString());
                    dto.setLeadCount((Long) r[1]);
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public LeadStatusSummaryResponse getLeadStatusSummary(Param param) {
        LeadStatusSummaryResponse response = new LeadStatusSummaryResponse();
        response.setNewCount(leadRepo.countByStatus(LeadStatus.NEW));
        response.setContactedCount(leadRepo.countByStatus(LeadStatus.CONTACTED));
        response.setQualifiedCount(leadRepo.countByStatus(LeadStatus.QUALIFIED));
        response.setLostCount(leadRepo.countByStatus(LeadStatus.LOST));
        return response;
    }
}