package com.erp.Service.TechnicianDevice;

import com.erp.Dto.Request.TechnicianDeviceRequest;
import com.erp.Model.TechnicianDevice;
import com.erp.Model.User;
import com.erp.Repository.TechnicianDevice.TechnicianDeviceRepository;
import com.erp.Repository.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TechnicianDeviceServiceImpl implements TechnicianDeviceService {

    private final TechnicianDeviceRepository technicianDeviceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public String saveDeviceToken(TechnicianDeviceRequest request) {
        TechnicianDevice device = technicianDeviceRepository
                .findByTechnician_IdAndFcmToken(request.getTechnicianId(), request.getFcmToken())
                .orElseGet(TechnicianDevice::new);

        User technician = userRepository.findByIdAndIsActiveTrue(request.getTechnicianId());
        device.setTechnician(technician);
        device.setFcmToken(request.getFcmToken());
        device.setPlatform(request.getPlatform());

        technicianDeviceRepository.save(device);

        return "Device token registered successfully";
    }
}
