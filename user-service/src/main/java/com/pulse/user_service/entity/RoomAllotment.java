package com.pulse.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_allotments")
public class RoomAllotment {

    public enum AllotmentStatus { ACTIVE, DISCHARGED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "admit_date")     private LocalDate admitDate;
    @Column(name = "discharge_date") private LocalDate dischargeDate;

    @Enumerated(EnumType.STRING) private AllotmentStatus status;

    private String reason;

    @Column(name = "created_at") private LocalDateTime createdAt;

    @PrePersist public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = AllotmentStatus.ACTIVE;
    }

    public Long getId()                  { return id; }
    public User getPatient()             { return patient; }
    public Room getRoom()                { return room; }
    public User getDoctor()              { return doctor; }
    public LocalDate getAdmitDate()      { return admitDate; }
    public LocalDate getDischargeDate()  { return dischargeDate; }
    public AllotmentStatus getStatus()   { return status; }
    public String getReason()            { return reason; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setId(Long id)                { this.id = id; }
    public void setPatient(User p)            { this.patient = p; }
    public void setRoom(Room r)               { this.room = r; }
    public void setDoctor(User d)             { this.doctor = d; }
    public void setAdmitDate(LocalDate d)     { this.admitDate = d; }
    public void setDischargeDate(LocalDate d) { this.dischargeDate = d; }
    public void setStatus(AllotmentStatus s)  { this.status = s; }
    public void setReason(String r)           { this.reason = r; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
}