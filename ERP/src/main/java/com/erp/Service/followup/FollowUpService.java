package com.erp.Service.followup;

import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Model.FollowUpDetails;


public interface FollowUpService {
    FollowUpDetails addOrUpdateFollowUp(FollowUpRequestDto request);
}
