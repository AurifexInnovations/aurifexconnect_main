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
public class StockTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Branch fromBranch;

    @ManyToOne
    private Branch toBranch;

    @ManyToOne
    private Inventory inventory;

    private double quantity;

    @Enumerated(EnumType.STRING)
    private StockTransferStatus status;

    private String approvedBy;

    @CreatedDate
    private LocalDateTime createdAt;
}
