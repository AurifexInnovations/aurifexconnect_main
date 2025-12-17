package com.erp.Model;

import com.erp.Dto.Response.ServiceTypeResponse;
import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity

@SqlResultSetMapping(
        name = "ServiceTypeResponseMapping",
        classes = @ConstructorResult(
                targetClass = ServiceTypeResponse.class,
                columns = {
                        @ColumnResult(name = "service_id", type = Long.class),
                        @ColumnResult(name = "service_name", type = String.class),
                        @ColumnResult(name = "service_description", type = String.class),
                        @ColumnResult(name = "service_price", type = BigDecimal.class),
                        @ColumnResult(name = "service_status", type = String.class),
                        @ColumnResult(name = "service_category", type = String.class),
                        @ColumnResult(name = "created_at", type = LocalDateTime.class),
                        @ColumnResult(name = "last_modified_at", type = LocalDateTime.class)
                }
        )
)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private long serviceId;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "service_price")
    private BigDecimal servicePrice;

    @Column(name = "residential_price")
    private BigDecimal residentialPrice;

    @Column(name = "commercial_price")
    private BigDecimal commercialPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_status")
    private ServiceStatus serviceStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_category")
    private ServiceCategory serviceCategory;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceDocuments> serviceDocuments;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToMany
    @JoinTable(
            name = "service_inventory",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<InventoryV2> inventories;
}
