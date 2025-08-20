package com.erp.Dto.Response;

import lombok.Data;

@Data
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private long timestamp;
    private String from;
    private String to;
    private boolean read;
    private Long readAt;
    private String type;
}
