package com.erp.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {
    private long id;
    private long inventoryId;
    private String action;
    private double quantity;
    private String performedBy;
    private LocalDateTime timeStamp;
}
