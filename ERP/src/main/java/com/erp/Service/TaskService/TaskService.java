package com.erp.Service.TaskService;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Model.TechnicianTaskMapper;
import com.erp.Projection.TechnicianResponse;
import com.erp.Projection.TechnicianTaskProjection;
import com.erp.Projection.TechnitianFeedbackDetailProjection;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskResponse addTask(TaskRequest taskRequest);

    List<GetAllTaskResponse> getAllTasks(Integer page, Integer size);

    List<TechnicianResponse> getTechnicians(TechnicianRequest technicianRequest);

    List<TechnicianTaskProjection> getTechniciansByDateAndAssigenDate(TechnicianTaskRequest technicianTaskRequest);

    boolean getTask(long taskId);


    public List<TechnicianLeaderboardDto> getTechnicianLeaderboard(String startDate, String endDate);

     void   updateTaskStatusTOInProgress(Long taskId,MultipartFile[] selfie);

     void updateTaskStatusToCompleted(Long taskId);

    void updateTaskMaterialForStatusProgress(Long taskId,
            CompleteTaskRequestDTO completeTaskRequestDTO,
            MultipartFile[] beforeImages,
            MultipartFile[] afterImages);

    List<TechnicianTaskMapper> getTechnitianByTaskId(long taskId);

    void updateTechnitianFeedBack(long taskId , long feedbackId);
    List<TechnitianFeedbackDetailProjection> getTechnitianFeedbackDetails(long feedbackId);
}
