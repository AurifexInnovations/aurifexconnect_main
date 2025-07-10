package com.erp.Model;

import com.erp.Enum.TaxName;
import com.erp.Enum.TaxType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private TaxName taxName;

    @Enumerated(EnumType.STRING)
    private TaxType taxType;

    private BigDecimal taxRate;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "taxes")
    private List<Inventory> inventories;

}