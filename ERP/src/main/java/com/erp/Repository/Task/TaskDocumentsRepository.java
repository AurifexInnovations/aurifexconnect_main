package com.erp.Repository.Task;

import com.erp.Model.TaskDocuments;
import com.erp.constants.FileUploadConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskDocumentsRepository extends JpaRepository<TaskDocuments, Long> {

    List<TaskDocuments> findByTask_TaskId(Long taskId);

    @Query("""
        SELECT td.documentUrl
        FROM TaskDocuments td
        WHERE td.task.taskId = :taskId
          AND td.documentType = :documentType
    """)
    List<String> findAllByTaskIdAndDocumentType(
            @Param("taskId") Long taskId,
            @Param("documentType") String documentType
    );

    void deleteAllByTask_TaskIdAndDocumentType(Long id, String documentType);
}
