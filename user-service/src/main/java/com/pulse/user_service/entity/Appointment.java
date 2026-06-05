package com.pulse.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    public enum AppointmentStatus { SCHEDULED, COMPLETED, CANCELLED, NO_SHOW }
    public enum AppointmentType   { CONSULTATION, FOLLOW_UP, EMERGENCY, ROUTINE_CHECKUP }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    private String reason;
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = AppointmentStatus.SCHEDULED;
    }

    // Getters
    public Long getId()                        { return id; }
    public User getPatient()                   { return patient; }
    public User getDoctor()                    { return doctor; }
    public LocalDate getAppointmentDate()      { return appointmentDate; }
    public LocalTime getAppointmentTime()      { return appointmentTime; }
    public AppointmentType getType()           { return type; }
    public AppointmentStatus getStatus()       { return status; }
    public String getReason()                  { return reason; }
    public String getNotes()                   { return notes; }
    public LocalDateTime getCreatedAt()        { return createdAt; }

    // Setters
    public void setId(Long id)                              { this.id = id; }
    public void setPatient(User patient)                    { this.patient = patient; }
    public void setDoctor(User doctor)                      { this.doctor = doctor; }
    public void setAppointmentDate(LocalDate d)             { this.appointmentDate = d; }
    public void setAppointmentTime(LocalTime t)             { this.appointmentTime = t; }
    public void setType(AppointmentType type)               { this.type = type; }
    public void setStatus(AppointmentStatus status)         { this.status = status; }
    public void setReason(String reason)                    { this.reason = reason; }
    public void setNotes(String notes)                      { this.notes = notes; }
    public void setCreatedAt(LocalDateTime createdAt)       { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Appointment apt = new Appointment();
        public Builder patient(User p)              { apt.patient = p; return this; }
        public Builder doctor(User d)               { apt.doctor = d; return this; }
        public Builder appointmentDate(LocalDate d) { apt.appointmentDate = d; return this; }
        public Builder appointmentTime(LocalTime t) { apt.appointmentTime = t; return this; }
        public Builder type(AppointmentType t)      { apt.type = t; return this; }
        public Builder status(AppointmentStatus s)  { apt.status = s; return this; }
        public Builder reason(String r)             { apt.reason = r; return this; }
        public Appointment build()                  { return apt; }
    }
}