package com.erp.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String title;
    private String message;
    private long timestamp;
    private String from;
    private String to;
}