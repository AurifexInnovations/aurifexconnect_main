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
    private String fileUrl;
    private int sequence;
    private String category;
    private long uploadedBy;
    private long genId;
    private String extension;
    private boolean active;
    private LocalDateTime uploadedAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}
