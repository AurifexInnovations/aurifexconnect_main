package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_logs")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name =  "inventory_id")
    private long inventoryId;
    @Column(name = "action", columnDefinition = "TEXT")
    private String action;
    @Column(name = "quantity")
    private double quantity;
    @Column(name =  "performed_by")
    private String performedBy;
    @Column(name =  "timeStamp")
    private LocalDateTime timeStamp;
}
