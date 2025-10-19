package com.erp.Dto.Request;

import com.erp.Enum.FieldType;
import com.erp.Enum.TaskCategory;
import com.erp.Enum.TaskStatus;
import com.erp.Enum.TechnicianTaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {


    private Long taskId;
    private String taskName;
    private Long customerId;
    private TaskCategory taskCategory;
    private TaskStatus taskStatus = TaskStatus.PENDING;
    private String taskDetails;
    private LocalDate assignedDate;
    private LocalTime assignedTime;

    private List<Long> serviceId;
    private String googleLocationLink;
    private TechnicianTaskStatus technicianTaskStatus = TechnicianTaskStatus.ASSIGNED;
    private FieldType fieldType;
    private String ServiceLocation;
    private List<Long>  technicianId;
    private  List<MaterialDto> materialDto ;

}
