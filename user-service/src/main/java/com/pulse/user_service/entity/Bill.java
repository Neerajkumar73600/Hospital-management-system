package com.pulse.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {

    public enum BillStatus { PENDING, PAID, PARTIALLY_PAID, CANCELLED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Column(name = "consultation_charge") private Double consultationCharge = 0.0;
    @Column(name = "room_charge")         private Double roomCharge         = 0.0;
    @Column(name = "lab_charge")          private Double labCharge          = 0.0;
    @Column(name = "pharmacy_charge")     private Double pharmacyCharge     = 0.0;
    @Column(name = "other_charges")       private Double otherCharges       = 0.0;
    @Column(name = "total_amount")        private Double totalAmount        = 0.0;
    @Column(name = "paid_amount")         private Double paidAmount         = 0.0;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    private String notes;

    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "paid_at")    private LocalDateTime paidAt;

    @PrePersist public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = BillStatus.PENDING;
    }

    public Long getId()                    { return id; }
    public User getPatient()               { return patient; }
    public Double getConsultationCharge()  { return consultationCharge; }
    public Double getRoomCharge()          { return roomCharge; }
    public Double getLabCharge()           { return labCharge; }
    public Double getPharmacyCharge()      { return pharmacyCharge; }
    public Double getOtherCharges()        { return otherCharges; }
    public Double getTotalAmount()         { return totalAmount; }
    public Double getPaidAmount()          { return paidAmount; }
    public BillStatus getStatus()          { return status; }
    public String getNotes()               { return notes; }
    public LocalDateTime getCreatedAt()    { return createdAt; }
    public LocalDateTime getPaidAt()       { return paidAt; }

    public void setId(Long id)                          { this.id = id; }
    public void setPatient(User p)                      { this.patient = p; }
    public void setConsultationCharge(Double c)         { this.consultationCharge = c; }
    public void setRoomCharge(Double r)                 { this.roomCharge = r; }
    public void setLabCharge(Double l)                  { this.labCharge = l; }
    public void setPharmacyCharge(Double p)             { this.pharmacyCharge = p; }
    public void setOtherCharges(Double o)               { this.otherCharges = o; }
    public void setTotalAmount(Double t)                { this.totalAmount = t; }
    public void setPaidAmount(Double p)                 { this.paidAmount = p; }
    public void setStatus(BillStatus s)                 { this.status = s; }
    public void setNotes(String n)                      { this.notes = n; }
    public void setCreatedAt(LocalDateTime c)           { this.createdAt = c; }
    public void setPaidAt(LocalDateTime p)              { this.paidAt = p; }
}