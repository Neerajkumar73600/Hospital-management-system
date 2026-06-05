package com.pulse.user_service.controller;

import com.pulse.user_service.entity.*;
import com.pulse.user_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PrescriptionRepository prescriptionRepo;
    private final UserRepository userRepo;

    @PostMapping("/prescribe")
    public ResponseEntity<Map<String, Object>> prescribe(@RequestBody Map<String, Object> req) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long patientId = ((Number) req.get("patientId")).longValue();
            Long doctorId = req.get("doctorId") != null ? ((Number) req.get("doctorId")).longValue() : null;

            User patient = userRepo.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

            Prescription p = new Prescription();
            p.setPatient(patient);
            p.setMedicineName((String) req.get("medicineName"));
            p.setDosage((String) req.get("dosage"));
            p.setFrequency((String) req.get("frequency"));
            p.setDurationDays(req.get("durationDays") != null ? ((Number) req.get("durationDays")).intValue() : 7);
            p.setInstructions((String) req.get("instructions"));
            p.setPrice(req.get("price") != null ? ((Number) req.get("price")).doubleValue() : 0.0);
            p.setStatus(Prescription.PrescriptionStatus.PENDING);

            if (doctorId != null) {
                userRepo.findById(doctorId).ifPresent(p::setDoctor);
            }

            prescriptionRepo.save(p);
            response.put("message", "Prescription added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Prescription>> getPendingPrescriptions() {
        return ResponseEntity.ok(prescriptionRepo.findByStatus(Prescription.PrescriptionStatus.PENDING));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionRepo.findByPatientId(patientId));
    }

    @PutMapping("/dispense/{id}")
    public ResponseEntity<Map<String, String>> dispense(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        return prescriptionRepo.findById(id).map(p -> {
            p.setStatus(Prescription.PrescriptionStatus.DISPENSED);
            prescriptionRepo.save(p);
            response.put("message", "Prescription dispensed");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Prescription not found");
            return ResponseEntity.badRequest().body(response);
        });
    }

    @GetMapping("/all")
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionRepo.findAll());
    }
}