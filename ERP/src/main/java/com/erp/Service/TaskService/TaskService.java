package com.erp.Service.TaskService;

import com.erp.Dto.Request.*;
import com.erp.Dto.Response.*;
import com.erp.Model.Task;
import com.erp.Model.TechnicianTaskMapper;
import com.erp.Projection.GetAllTaskResponse;
import com.erp.Projection.TechnicianResponse;
import com.erp.Projection.TechnicianTaskProjection;
import com.erp.Projection.TechnitianFeedbackDetailProjection;
import org.springframework.web.multipart.MultipartFile;

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

    OtpResponseDTO updateTaskMaterialForStatusProgress(Long taskId,
            CompleteTaskRequestDTO completeTaskRequestDTO,
            MultipartFile[] beforeImages,
            MultipartFile[] afterImages);

    List<TechnicianTaskMapper> getTechnitianByTaskId(long taskId);

    void updateTechnitianFeedBack(long taskId , long feedbackId);
    List<TechnitianFeedbackDetailProjection> getTechnitianFeedbackDetails(long feedbackId);

     String  updateTaskLocation(Long taskId, TaskLocationUpdateRequest request) ;

    ResultDto<TechnicianLeaderboardDto> getTechnicianLeaderboard(PaginationRequest paginationRequest);

    ResultDto<GetAllTaskResponse> getAllTasks();

    ResultDto<TechnicianResponseDTO> searchTasks(FilterRequest filterRequest);

}
