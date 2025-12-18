package com.erp.Dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechTrackDto {
    private Long technicianId;
    private Double latitude;
    private Double longitude;
}
