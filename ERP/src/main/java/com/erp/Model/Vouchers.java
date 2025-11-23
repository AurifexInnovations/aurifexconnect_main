package com.erp.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vouchers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voucherId;

    @Column(name = "voucher_name", nullable = false, length = 100)
    private String voucherName;

    @Column(name = "voucher_code", nullable = false, unique = true, length = 50)
    private String voucherCode;

    @Column(name = "voucher_category", nullable = false, length = 50)
    private String voucherCategory; // Receipt / Payment / Journal

    @Column(name = "voucher_applies_to", columnDefinition = "jsonb")
    private String voucherAppliesTo; // JSON array stored as String

    @Column(name = "default_series_prefix", length = 20)
    private String defaultSeriesPrefix;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "updated_by")
    private Integer updatedBy;
}