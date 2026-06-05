package com.pulse.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
public class Room {

    public enum RoomType   { GENERAL, PRIVATE, ICU, EMERGENCY, OPERATION }
    public enum RoomStatus { AVAILABLE, OCCUPIED, MAINTENANCE }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Enumerated(EnumType.STRING) private RoomType type;
    @Enumerated(EnumType.STRING) private RoomStatus status;

    private Integer floor;
    private Double pricePerDay;
    private String description;

    @Column(name = "created_at") private LocalDateTime createdAt;

    @PrePersist public void prePersist() {
        createdAt = LocalDateTime.now();
        if (status == null) status = RoomStatus.AVAILABLE;
    }

    public Long getId()            { return id; }
    public String getRoomNumber()  { return roomNumber; }
    public RoomType getType()      { return type; }
    public RoomStatus getStatus()  { return status; }
    public Integer getFloor()      { return floor; }
    public Double getPricePerDay() { return pricePerDay; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id)               { this.id = id; }
    public void setRoomNumber(String r)      { this.roomNumber = r; }
    public void setType(RoomType t)          { this.type = t; }
    public void setStatus(RoomStatus s)      { this.status = s; }
    public void setFloor(Integer f)          { this.floor = f; }
    public void setPricePerDay(Double p)     { this.pricePerDay = p; }
    public void setDescription(String d)     { this.description = d; }
    public void setCreatedAt(LocalDateTime c){ this.createdAt = c; }
}