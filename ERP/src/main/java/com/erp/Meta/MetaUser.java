//package com.erp.Meta;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@EntityListeners(AuditingEntityListener.class)
//public class MetaUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long metaUserId;
//
//    @Column(name = "user_email", nullable = false, unique = true)
//    private String userEmail;
//
//    @Column(name = "schema_name", nullable = false, unique = true)
//    private String schemaName;
//
//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime lastModifiedAt;
//}
