package com.erp.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lead_service_mapper")
public class LeadServiceMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_service_id")
    private Long leadServiceId;

    @Column(name = "lead_id", nullable = false)
    private Long leadId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;
}
