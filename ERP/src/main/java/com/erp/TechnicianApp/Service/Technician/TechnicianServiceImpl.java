package com.erp.TechnicianApp.Service.Technician;


import com.erp.TechnicianApp.Dto.Request.CommonParam;
import com.erp.TechnicianApp.Dto.Request.TechnicianRequest;
import com.erp.TechnicianApp.Dto.Response.TechnicianResponse;
import com.erp.TechnicianApp.Exception.TechnicianNotFoundById;
import com.erp.TechnicianApp.Mapper.Technician.TechnicianMapper;
import com.erp.TechnicianApp.Model.Technician.Technician;
import com.erp.TechnicianApp.Repository.Technician.TechnicianRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final TechnicianMapper technicianMapper;

    @Override
    public TechnicianResponse addTechnician(TechnicianRequest technicianRequest) {
        Technician technician = technicianMapper.mapToEntity(technicianRequest);
        Technician savedTechnician = technicianRepository.save(technician);
        return technicianMapper.mapToResponse(savedTechnician);
    }

    @Override
    public List<TechnicianResponse> getAllTechnician() {
        List<Technician> technicians = technicianRepository.findAll();
        List<TechnicianResponse> technicianResponses = new ArrayList<>();
        for (Technician technician : technicians) {
            TechnicianResponse technicianResponse = technicianMapper.mapToResponse(technician);
            technicianResponses.add(technicianResponse);
        }
        return technicianResponses;
    }

    @Override
    public List<TechnicianResponse> findTechnicianById(CommonParam param) {
        List<Technician> technicians = technicianRepository.findByTechnicianIdOrTechnicianName(param.getId(), param.getName());
        if (technicians.isEmpty()) {
            throw new TechnicianNotFoundById("Technician Not found Against given Detail");
        } else {
            return technicianMapper.mapToResponse(technicians);

        }
    }

    @Override
    public TechnicianResponse updateTechnicianById(long id, TechnicianRequest technicianRequest) {
        Optional<Technician> optionalTechnician = technicianRepository.findById(id);
        if (optionalTechnician.isPresent()) {
            Technician technician = optionalTechnician.get();
            technicianMapper.updateTechnicianFromRequest(technicianRequest, technician);
            Technician updateTechnician = technicianRepository.save(technician);
            return technicianMapper.mapToResponse(updateTechnician);
        } else {
            throw new TechnicianNotFoundById("Can not Update: Technician Id Not found " + id);
        }
    }

    @Override
    public TechnicianResponse deleteTechnicianById(long id) {
        Optional<Technician> optionalTechnician = technicianRepository.findById(id);
        if (optionalTechnician.isPresent()) {
            Technician technician = optionalTechnician.get();
            technicianRepository.delete(technician);
            return technicianMapper.mapToResponse(technician);
        } else {
            throw new TechnicianNotFoundById("Can not Delete: Id Not Found Please check with your DataBase" + id);
        }
    }
}
