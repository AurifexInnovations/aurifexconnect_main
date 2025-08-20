package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false)
    private String message;

    private long timestamp;

    @Column(name = "from_user")
    private String from;

    @Column(name = "to_user")
    private String to;

    @Column(name = "is_read")
    private boolean read = false;

    @Column(name = "read_at")
    private Long readAt;

    @Column(name = "type")
    private String type;
}
