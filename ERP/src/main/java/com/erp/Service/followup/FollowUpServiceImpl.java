package com.erp.Service.followup;


import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Dto.Response.FollowUpResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Exception.Branch_Exception.BranchNotFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.followup.FollowUpMapper;
import com.erp.Model.Branch;
import com.erp.Model.FollowUpDetails;
import com.erp.Model.Leads;
import com.erp.Repository.Branch.BranchRepository;
import com.erp.Repository.Lead.LeadRepositorys;
import com.erp.Repository.User.UserRepository;
import com.erp.Repository.followup.FollowUpDetailsRepository;
import com.erp.Security.util.UserIdentity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FollowUpServiceImpl implements com.erp.Service.followup.FollowUpService {

    private final FollowUpDetailsRepository followUpRepository;
    private final FollowUpMapper mapper;
    private final BranchRepository branchRepository;
    private final UserIdentity userIdentity;
    private final UserRepository userRepository;
    private final LeadRepositorys leadRepositorys;

//    @Override
//    @Transactional
//    public FollowUpDetails addOrUpdateFollowUp(FollowUpRequestDto request) {
//        log.info("Received request to add/update follow-up: {}", request);
//
//        FollowUpDetails followUp;
//
//        if (request.getId() != null) {
//            followUp = followUpRepository.findById(request.getId())
//                    .orElseThrow(() -> new RuntimeException("Follow-up not found with ID: " + request.getId()));
//            mapper.updateEntity(followUp, request);
//            log.info("Updating existing follow-up with ID: {}", request.getId());
//        } else {
//            followUp = mapper.toEntity(request);
//            log.info("Creating new follow-up for lead_id: {}", request.getLeadId());
//        }
//
//        FollowUpDetails saved = followUpRepository.save(followUp);
//        log.info("Follow-up saved successfully with ID: {}", saved.getId());
//        return saved;
//    }


    @Override
    public FollowUpResponseDto addFollowUp(FollowUpRequestDto request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found !!"));

        FollowUpDetails followUpDetails = mapper.toEntityDto(request);
        followUpDetails.setBranch(branch);

        FollowUpDetails saved = followUpRepository.save(followUpDetails);
        return toResponseDto(saved);
    }


    @Override
    public ResultDto<FollowUpResponseDto> getAllFollowUps() {

        List<FollowUpResponseDto> responseDtos = new ArrayList<>();
        for(FollowUpDetails follow : followUpRepository.findAll()){
            responseDtos.add(toResponseDto(follow));
        }

        ResultDto<FollowUpResponseDto> resultDto = new ResultDto<>();
        resultDto.setResults(responseDtos);
        resultDto.setCount(responseDtos.size());

        return resultDto;
    }


    @Override
    public FollowUpResponseDto getById(long id) {
        FollowUpDetails followUpDetails = followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow Up Not Found !!"));

        return toResponseDto(followUpDetails);
    }


    @Override
    public FollowUpResponseDto deleteById(long id) {
        FollowUpDetails followUpDetails = followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Follow Up Not Found !!"));

        followUpRepository.delete(followUpDetails);

        return toResponseDto(followUpDetails);
    }


    @Override
    public FollowUpResponseDto updateById(FollowUpRequestDto request) {
        FollowUpDetails followUpDetails = mapper.toEntityDto(request);

        if(request.getStatus().equalsIgnoreCase("LOST") && request.getLeadId() != null)
        {
            Leads leads = leadRepositorys.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead Not Found !!"));

            leads.setLeadStatus("LOST");
            leads.setLostReason(request.getLostReason());

            leadRepositorys.save(leads);
        }

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException("Branch Not Found !!"));

        followUpDetails.setBranch(branch);
        FollowUpDetails saved = followUpRepository.save(followUpDetails);
        return toResponseDto(saved);
    }


    private FollowUpResponseDto toResponseDto(FollowUpDetails follow){
        FollowUpResponseDto followUpResponseDto = mapper.toResponseDto(follow);
        followUpResponseDto.setBranchId(follow.getBranch().getBranchId());
        return followUpResponseDto;
    }
}
