package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_otp")
@Data
public class UserOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name =  "otp", length = 10)
    private String otp;

    @Column(name = "otp_generated_date_time")
    private LocalDateTime otpGeneratedDateTime;

    @Column(name = "otp_expiry_date_time")
    private LocalDateTime otpExpiryDateTime;

    @Column(name = "lock_till_date_time")
    private LocalDateTime lockTillDateTime;

    @Column(name = "max_attemp")
    private Integer maxAttemp;

    @Column(name = "is_verified")
    private Boolean isVerified;
}
