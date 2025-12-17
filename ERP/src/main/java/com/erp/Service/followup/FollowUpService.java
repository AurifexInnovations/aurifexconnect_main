package com.erp.Service.followup;

import com.erp.Dto.Request.FollowUpRequestDto;
import com.erp.Dto.Response.FollowUpResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.FollowUpDetails;


public interface FollowUpService {
    FollowUpResponseDto addFollowUp(FollowUpRequestDto request);

    ResultDto<FollowUpResponseDto> getAllFollowUps();

    FollowUpResponseDto getById(long id);

    FollowUpResponseDto deleteById(long id);

    FollowUpResponseDto updateById(FollowUpRequestDto requestDto);

//    FollowUpDetails addOrUpdateFollowUp(FollowUpRequestDto request);
}
