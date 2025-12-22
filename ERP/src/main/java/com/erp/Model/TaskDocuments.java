package com.erp.Model;

import com.erp.constants.FileUploadConstants;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "task_documents")
@Data
public class TaskDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_url", nullable = false)
    private String documentUrl;   // S3 key

    @Column(name = "document_name")
    private String documentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "document_type")
    private String documentType;
}
