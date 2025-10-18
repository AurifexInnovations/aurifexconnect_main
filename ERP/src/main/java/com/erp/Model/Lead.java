package com.erp.Model;

import com.erp.Config.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lead_details")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lead extends AbstractAuditable<Admin, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lead_details_seq_gen")
    @SequenceGenerator(name = "lead_details_seq_gen", sequenceName = "lead_details_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "contact")
    private String contact;

    @Column(name = "status")
    private String status;

    @Column(name = "source")
    private String source;

    @Column(name = "teg")
    private String teg;

    @Column(name = "engagement")
    private String engagement;

    @Column(name = "next_follow_up_date")
    private LocalDateTime nextFollowUpDate;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

}
