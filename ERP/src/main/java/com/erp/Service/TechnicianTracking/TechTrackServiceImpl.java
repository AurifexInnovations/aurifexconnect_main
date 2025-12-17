package com.erp.Service.TechnicianTracking;

import com.erp.Dto.Request.TechTrackDto;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Exception.User.UserNotFoundException;
import com.erp.Model.TechTrack;
import com.erp.Repository.TechnicianTracking.TechTrackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TechTrackServiceImpl implements TechTrackService {

    private final TechTrackRepository techTrackRepository;

    @Override
    public TechTrackDto updateLocation(TechTrackDto techTrackDto) {

        TechTrack techTrack = techTrackRepository
                .findById(techTrackDto.getTechnicianId())
                .orElseGet(() -> {
                    TechTrack t = new TechTrack();
                    t.setTechnicianId(techTrackDto.getTechnicianId());
                    return t;
                });

        techTrack.setLatitude(techTrackDto.getLatitude());
        techTrack.setLongitude(techTrackDto.getLongitude());

        TechTrack saved = techTrackRepository.save(techTrack);

        return toResponseDto(saved);
    }


    @Override
    public TechTrackDto findById(Long technicianId) {
        TechTrack techTrack = techTrackRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Technician Not Found !!"));

        return toResponseDto(techTrack);
    }

    private TechTrackDto toResponseDto(TechTrack techTrack){
        TechTrackDto techTrackDto = new TechTrackDto();

        techTrackDto.setTechnicianId(techTrack.getTechnicianId());
        techTrackDto.setLatitude(techTrack.getLatitude());
        techTrackDto.setLongitude(techTrack.getLongitude());

        return techTrackDto;
    }
}
