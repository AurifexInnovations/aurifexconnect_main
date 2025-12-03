package com.erp.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "otp_verification")
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String otp;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
