package com.erp.Service.TechnicianTracking;

import com.erp.Dto.Request.TechTrackDto;

public interface TechTrackService {
    TechTrackDto updateLocation(TechTrackDto techTrackDto);

    TechTrackDto findById(Long technicianId);
}
