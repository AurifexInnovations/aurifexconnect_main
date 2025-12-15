package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Setter
@Table(name = "quotation_services")
public class QuotationServiceMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotation_service_id")
    private Long quotationServiceId;

    @Column(name = "quotation_id", nullable = false)
    private Long quotationId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

}
