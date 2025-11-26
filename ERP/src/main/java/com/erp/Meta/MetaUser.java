package com.erp.Meta;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Table(name = "meta_user", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class MetaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "schema_name", nullable = false, unique = true)
    private String schemaName;
}
