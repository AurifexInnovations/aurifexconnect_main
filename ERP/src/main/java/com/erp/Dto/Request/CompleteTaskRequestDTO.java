package com.erp.Dto.Request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskRequestDTO {


    private FeedbackRequest feedbackList;
    private List<TaskMaterialDTO> taskMaterialList;

}
