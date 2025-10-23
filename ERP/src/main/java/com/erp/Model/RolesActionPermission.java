package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles_action_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolesActionPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "module_id")
    private Long moduleId;

    @Column(name = "action_id")
    private Long action;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
