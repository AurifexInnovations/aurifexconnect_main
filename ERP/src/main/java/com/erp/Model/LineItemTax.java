package com.erp.Model;

import com.erp.Enum.TaxName;
import com.erp.Enum.TaxType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class LineItemTax {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal taxRate;
    private double taxAmount;

    @ManyToOne
    private LineItems lineItems;

    @ManyToOne
    private Tax tax;


}
