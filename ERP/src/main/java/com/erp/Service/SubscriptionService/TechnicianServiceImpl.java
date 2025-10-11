package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.TechnicianRequest;
import com.erp.Dto.Response.TechnicianResponse;
import com.erp.Dto.SubscriptionsDto.TechnicianDto;
import com.erp.Mapper.SubscriptionModule.TechnicianMapper;
import com.erp.Model.TechnicianEntity;
import com.erp.Repository.SubscriptionModule.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class TechnicianServiceImpl implements ITechnicianService {

    @Autowired
    private TechnicianRepository technicianRepository;

    @Override
    public TechnicianDto fetchTechnicianById(Long technicianId) {
        try {
            TechnicianEntity entity = technicianRepository.findByTechnicianId(technicianId).orElse(null);
            return entity != null ? TechnicianMapper.toDto(entity) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public TechnicianResponse createTechnician(TechnicianRequest request) {
        TechnicianEntity entity = new TechnicianEntity();
        entity.setTechnicianName(request.getTechnicianName());
        entity.setContact(request.getContact());
        entity.setAge(request.getAge());
        entity.setGender(request.getGender());
        entity.setBranchCode(request.getBranchCode());
        entity.setCreatedBy(request.getCreatedBy());
        entity.setActiveYn("Y");
        entity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        TechnicianEntity saved = technicianRepository.save(entity);

        TechnicianResponse response = new TechnicianResponse();
        response.setTechnicianId(saved.getTechnicianId());
        response.setTechnicianName(saved.getTechnicianName());
        response.setContact(saved.getContact());
        response.setGender(saved.getGender());
        response.setActiveYn(saved.getActiveYn());

        return response;
    }

}