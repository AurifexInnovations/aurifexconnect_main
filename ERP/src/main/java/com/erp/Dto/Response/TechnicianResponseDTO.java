package com.erp.Dto.Response;

import com.erp.Dto.Request.MaterialDto;
import com.erp.Enum.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TechnicianResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String customerPhone;
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

    private List<String> afterImagerUrl;
    private List<String> beforeImageUrl;
    private List<String> salfie;

    private Long taskId;

    // ===============================
    // MATERIAL FIELDS (ADDED)
    // ===============================
    private List<MaterialDtoResponse> materials;

}
