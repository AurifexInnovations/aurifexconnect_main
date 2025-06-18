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
    private TaxName taxName;  // Example: "CGST", "SGST", "IGST", "Luxury Tax"

    @Enumerated(EnumType.STRING)
    private TaxType taxType; // Example: PERCENTAGE, FIXED_AMOUNT

    private BigDecimal taxRate; // Example: 9.0, 18.0

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "taxes")
    private List<Inventory> inventories;

}
