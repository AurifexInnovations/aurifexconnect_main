package com.erp.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "technician_assignment")
public class TechnicianAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "technician_id")
    private String technicianId;

    private String frequency;
    private String duration;

    @ManyToOne
    @JoinColumn(name = "quotation_id", referencedColumnName = "quotation_id")
    private Quotation quotation;

    // Getters & Setters
    public Long getId() { return id; }
    public String getTechnicianId() { return technicianId; }
    public void setTechnicianId(String technicianId) { this.technicianId = technicianId; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public Quotation getQuotation() { return quotation; }
    public void setQuotation(Quotation quotation) { this.quotation = quotation; }
}
