package com.erp.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "masterConfig")
@Data
public class MasterConfig {

    @Id
    @Column(name = "masterConfigId")
    private Long masterConfigId;

    @Column(name = "serviceName", nullable = false, unique = true)
    private String serviceName;

    @Column(name = "jsonData")
    private String jsonData;

    @Column(name = "userid", nullable = false)
    private String userid;

    @Column(name = "activeYn", length = 1)
    private String activeYn;

    @Column(name = "createdOn", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdOn;

    @Column(name = "updatedOn", columnDefinition = "TIMESTAMP DEFAULT NULL")
    private LocalDateTime updatedOn;

}