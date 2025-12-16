package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quotation_service")
public class QuotationService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quotation_id", nullable = false)
    private Long quotationId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

}
