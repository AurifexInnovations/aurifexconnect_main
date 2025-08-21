package com.erp.Service.TaskActivity;

import com.erp.Dto.Request.TaskActivityRequest;
import com.erp.Dto.Response.TaskActivityResponse;

import java.time.LocalDate;
import java.util.List;

public interface TaskActivityService {

    TaskActivityResponse create(TaskActivityRequest request);

    TaskActivityResponse update(Long id, TaskActivityRequest request);

    void delete(Long id);

    TaskActivityResponse getById(Long id);

    List<TaskActivityResponse> getAll();

    List<TaskActivityResponse> getByStatus(String status);

    List<TaskActivityResponse> getByType(String type);

    List<TaskActivityResponse> getByDueDate(LocalDate date);

    List<TaskActivityResponse> getByRelatedLead(Long leadId);

    List<TaskActivityResponse> getByRelatedContact(Long contactId);

    List<TaskActivityResponse> getByRelatedDeal(Long dealId);
}
