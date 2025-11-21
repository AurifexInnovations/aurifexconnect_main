package com.erp.Dto.Response;

import com.erp.Enum.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TechnicianResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String category;
    private String designation;
    private TaskStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;

    private String location;
    private LocalDate assignedDate;
    private String googleLocationLink;
    private String taskName;
    private Long serviceId;
    private String serviceName;
    private Double latitude;
    private Double longitude;
    private String customerName;
    private String customerAddress;
}
