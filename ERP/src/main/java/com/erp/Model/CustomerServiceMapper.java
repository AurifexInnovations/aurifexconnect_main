package com.erp.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_service_mapper")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerServiceMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_service_id")
    private Long customerServiceId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;
}
