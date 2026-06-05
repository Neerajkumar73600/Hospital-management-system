package com.pulse.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    public enum PrescriptionStatus { PENDING, DISPENSED, CANCELLED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    private String dosage;
    private String frequency;
    private Integer durationDays;
    private String instructions;
    private Double price;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;

    @Column(name = "created_at") private LocalDateTime createdAt;

    @PrePersist public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = PrescriptionStatus.PENDING;
    }

    public Long getId()                  { return id; }
    public User getPatient()             { return patient; }
    public User getDoctor()              { return doctor; }
    public String getMedicineName()      { return medicineName; }
    public String getDosage()            { return dosage; }
    public String getFrequency()         { return frequency; }
    public Integer getDurationDays()     { return durationDays; }
    public String getInstructions()      { return instructions; }
    public Double getPrice()             { return price; }
    public PrescriptionStatus getStatus(){ return status; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setId(Long id)                       { this.id = id; }
    public void setPatient(User p)                   { this.patient = p; }
    public void setDoctor(User d)                    { this.doctor = d; }
    public void setMedicineName(String m)            { this.medicineName = m; }
    public void setDosage(String d)                  { this.dosage = d; }
    public void setFrequency(String f)               { this.frequency = f; }
    public void setDurationDays(Integer d)           { this.durationDays = d; }
    public void setInstructions(String i)            { this.instructions = i; }
    public void setPrice(Double p)                   { this.price = p; }
    public void setStatus(PrescriptionStatus s)      { this.status = s; }
    public void setCreatedAt(LocalDateTime c)        { this.createdAt = c; }
}