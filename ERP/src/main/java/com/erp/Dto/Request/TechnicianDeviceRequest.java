package com.erp.Dto.Request;

import com.erp.Enum.Platform;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDeviceRequest {
    private Long technicianId;
    private String fcmToken;
    private Platform platform;
}
