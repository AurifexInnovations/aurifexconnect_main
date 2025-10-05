package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "files")
public class File {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "file_url")
    private String fileUrl;
    @Column(name = "sequence")
    private int sequence;
    @Column(name = "category")
    private String category;
    @Column(name = "uploaded_by")
    private long uploadedBy;
    @Column(name = "gen_id")
    private long genId;
    @Column(name = "extension")
    private String extension;
    @Column(name = "active")
    private boolean active;
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
