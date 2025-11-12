package com.erp.Model;

import com.erp.Enum.StockTransferStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stocktransfer")
public class StockTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "from_branch_id")  // match your DB column
    private Branch fromBranch;

    @ManyToOne
    @JoinColumn(name = "to_branch_id")    // match your DB column
    private Branch toBranch;

    @ManyToOne
    private Inventory inventory;

    @Column(name = "quantity")
    private double quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StockTransferStatus status;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "initiated_by")
    private String initiatedBy;

    @Column(name = "reason")
    private String reason;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
