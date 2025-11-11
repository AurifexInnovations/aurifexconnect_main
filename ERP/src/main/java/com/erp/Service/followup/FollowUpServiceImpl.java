package com.erp.Service.followup;


import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Mapper.followup.FollowUpMapper;
import com.erp.Model.FollowUpDetails;
import com.erp.Repository.followup.FollowUpDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class FollowUpServiceImpl implements com.erp.Service.followup.FollowUpService {

    private final FollowUpDetailsRepository followUpRepository;
    private final FollowUpMapper mapper;

    @Override
    @Transactional
    public FollowUpDetails addOrUpdateFollowUp(FollowUpRequestDto request) {
        log.info("Received request to add/update follow-up: {}", request);

        FollowUpDetails followUp;

        if (request.getId() != null) {
            followUp = followUpRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Follow-up not found with ID: " + request.getId()));
            mapper.updateEntity(followUp, request);
            log.info("Updating existing follow-up with ID: {}", request.getId());
        } else {
            followUp = mapper.toEntity(request);
            log.info("Creating new follow-up for lead_id: {}", request.getLeadId());
        }

        FollowUpDetails saved = followUpRepository.save(followUp);
        log.info("Follow-up saved successfully with ID: {}", saved.getId());
        return saved;
    }
}
