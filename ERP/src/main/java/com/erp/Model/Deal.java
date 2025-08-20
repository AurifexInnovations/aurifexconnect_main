package com.erp.Model;

import com.erp.Enum.DealStage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private DealStage stage = DealStage.PROSPECTING;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "assigned_to_id")
    private Long assignedToId;

    @Column(name = "assigned_to_name")
    private String assignedToName;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}