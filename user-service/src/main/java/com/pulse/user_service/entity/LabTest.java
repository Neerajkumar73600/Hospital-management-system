package com.pulse.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_tests")
public class LabTest {

    public enum TestStatus { PENDING, IN_PROGRESS, COMPLETED, CANCELLED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lab_tech_id")
    private User labTech;

    @Column(name = "test_name", nullable = false)
    private String testName;

    private String description;
    private String result;
    private String notes;
    private Double price;

    @Enumerated(EnumType.STRING)
    private TestStatus status;

    @Column(name = "created_at")     private LocalDateTime createdAt;
    @Column(name = "completed_at")   private LocalDateTime completedAt;

    @PrePersist public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = TestStatus.PENDING;
    }

    public Long getId()               { return id; }
    public User getPatient()          { return patient; }
    public User getDoctor()           { return doctor; }
    public User getLabTech()          { return labTech; }
    public String getTestName()       { return testName; }
    public String getDescription()    { return description; }
    public String getResult()         { return result; }
    public String getNotes()          { return notes; }
    public Double getPrice()          { return price; }
    public TestStatus getStatus()     { return status; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    public void setId(Long id)                  { this.id = id; }
    public void setPatient(User p)              { this.patient = p; }
    public void setDoctor(User d)               { this.doctor = d; }
    public void setLabTech(User l)              { this.labTech = l; }
    public void setTestName(String t)           { this.testName = t; }
    public void setDescription(String d)        { this.description = d; }
    public void setResult(String r)             { this.result = r; }
    public void setNotes(String n)              { this.notes = n; }
    public void setPrice(Double p)              { this.price = p; }
    public void setStatus(TestStatus s)         { this.status = s; }
    public void setCreatedAt(LocalDateTime c)   { this.createdAt = c; }
    public void setCompletedAt(LocalDateTime c) { this.completedAt = c; }
}