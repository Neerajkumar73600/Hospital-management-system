package com.pulse.user_service.controller;

import com.pulse.user_service.entity.*;
import com.pulse.user_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/lab")
@RequiredArgsConstructor
public class LabController {

    private final LabTestRepository labTestRepo;
    private final UserRepository userRepo;

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> requestTest(@RequestBody Map<String, Object> req) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long patientId = ((Number) req.get("patientId")).longValue();
            Long doctorId  = req.get("doctorId") != null ? ((Number) req.get("doctorId")).longValue() : null;

            User patient = userRepo.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

            LabTest test = new LabTest();
            test.setPatient(patient);
            test.setTestName((String) req.get("testName"));
            test.setDescription((String) req.get("description"));
            test.setPrice(req.get("price") != null ? ((Number) req.get("price")).doubleValue() : 0.0);
            test.setStatus(LabTest.TestStatus.PENDING);

            if (doctorId != null) {
                userRepo.findById(doctorId).ifPresent(test::setDoctor);
            }

            LabTest saved = labTestRepo.save(test);
            response.put("message", "Lab test requested successfully");
            response.put("testId", saved.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<LabTest>> getAllTests() {
        return ResponseEntity.ok(labTestRepo.findAll());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LabTest>> getPendingTests() {
        return ResponseEntity.ok(labTestRepo.findByStatus(LabTest.TestStatus.PENDING));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabTest>> getPatientTests(@PathVariable Long patientId) {
        return ResponseEntity.ok(labTestRepo.findByPatientId(patientId));
    }

    @PutMapping("/complete/{testId}")
    public ResponseEntity<Map<String, String>> completeTest(
            @PathVariable Long testId,
            @RequestBody Map<String, String> req) {
        Map<String, String> response = new HashMap<>();
        return labTestRepo.findById(testId).map(test -> {
            test.setResult(req.get("result"));
            test.setNotes(req.get("notes"));
            test.setStatus(LabTest.TestStatus.COMPLETED);
            test.setCompletedAt(LocalDateTime.now());
            labTestRepo.save(test);
            response.put("message", "Test completed successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Test not found");
            return ResponseEntity.badRequest().body(response);
        });
    }
}