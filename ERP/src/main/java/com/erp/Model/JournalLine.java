package com.erp.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "journal_lines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lineId;

    @Column(name = "journal_id", nullable = false)
    private Integer journalId;

    @Column(name = "coa_id", nullable = false)
    private Integer coaId;

    @Column(precision = 15, scale = 2)
    private Double debit = 0.00;

    @Column(precision = 15, scale = 2)
    private Double credit = 0.00;

    @Column(columnDefinition = "TEXT")
    private String lineDescription;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "updated_by")
    private Integer updatedBy;
}
