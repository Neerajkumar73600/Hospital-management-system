package com.pulse.user_service.controller;

import com.pulse.user_service.entity.*;
import com.pulse.user_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepo;
    private final RoomAllotmentRepository allotmentRepo;
    private final UserRepository userRepo;

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomRepo.findAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        return ResponseEntity.ok(roomRepo.findByStatus(Room.RoomStatus.AVAILABLE));
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addRoom(@RequestBody Map<String, Object> req) {
        Map<String, Object> response = new HashMap<>();
        try {
            Room room = new Room();
            room.setRoomNumber((String) req.get("roomNumber"));
            room.setType(Room.RoomType.valueOf((String) req.get("type")));
            room.setFloor(((Number) req.get("floor")).intValue());
            room.setPricePerDay(((Number) req.get("pricePerDay")).doubleValue());
            room.setDescription((String) req.get("description"));
            room.setStatus(Room.RoomStatus.AVAILABLE);
            Room saved = roomRepo.save(room);
            response.put("message", "Room added successfully");
            response.put("roomId", saved.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/allot")
    public ResponseEntity<Map<String, Object>> allotRoom(@RequestBody Map<String, Object> req) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long patientId = ((Number) req.get("patientId")).longValue();
            Long roomId    = ((Number) req.get("roomId")).longValue();
            Long doctorId  = req.get("doctorId") != null ? ((Number) req.get("doctorId")).longValue() : null;

            User patient = userRepo.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
            Room room    = roomRepo.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

            RoomAllotment allotment = new RoomAllotment();
            allotment.setPatient(patient);
            allotment.setRoom(room);
            allotment.setAdmitDate(LocalDate.now());
            allotment.setReason((String) req.get("reason"));
            allotment.setStatus(RoomAllotment.AllotmentStatus.ACTIVE);

            if (doctorId != null) {
                userRepo.findById(doctorId).ifPresent(allotment::setDoctor);
            }

            room.setStatus(Room.RoomStatus.OCCUPIED);
            roomRepo.save(room);
            allotmentRepo.save(allotment);

            response.put("message", "Room allotted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/discharge/{allotmentId}")
    public ResponseEntity<Map<String, String>> discharge(@PathVariable Long allotmentId) {
        Map<String, String> response = new HashMap<>();
        return allotmentRepo.findById(allotmentId).map(a -> {
            a.setStatus(RoomAllotment.AllotmentStatus.DISCHARGED);
            a.setDischargeDate(LocalDate.now());
            a.getRoom().setStatus(Room.RoomStatus.AVAILABLE);
            roomRepo.save(a.getRoom());
            allotmentRepo.save(a);
            response.put("message", "Patient discharged successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Allotment not found");
            return ResponseEntity.badRequest().body(response);
        });
    }

    @GetMapping("/allotments")
    public ResponseEntity<List<RoomAllotment>> getAllAllotments() {
        return ResponseEntity.ok(allotmentRepo.findAll());
    }

    @GetMapping("/allotments/active")
    public ResponseEntity<List<RoomAllotment>> getActiveAllotments() {
        return ResponseEntity.ok(allotmentRepo.findByStatus(RoomAllotment.AllotmentStatus.ACTIVE));
    }

    @GetMapping("/allotments/patient/{patientId}")
    public ResponseEntity<List<RoomAllotment>> getPatientAllotments(@PathVariable Long patientId) {
        return ResponseEntity.ok(allotmentRepo.findByPatientId(patientId));
    }
}
