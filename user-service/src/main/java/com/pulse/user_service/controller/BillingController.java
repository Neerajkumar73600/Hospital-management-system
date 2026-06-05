package com.pulse.user_service.controller;

import com.pulse.user_service.entity.*;
import com.pulse.user_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillRepository billRepo;
    private final UserRepository userRepo;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateBill(@RequestBody Map<String, Object> req) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long patientId = ((Number) req.get("patientId")).longValue();
            User patient = userRepo.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

            Bill bill = new Bill();
            bill.setPatient(patient);
            bill.setConsultationCharge(req.get("consultationCharge") != null ? ((Number) req.get("consultationCharge")).doubleValue() : 0.0);
            bill.setRoomCharge(req.get("roomCharge") != null ? ((Number) req.get("roomCharge")).doubleValue() : 0.0);
            bill.setLabCharge(req.get("labCharge") != null ? ((Number) req.get("labCharge")).doubleValue() : 0.0);
            bill.setPharmacyCharge(req.get("pharmacyCharge") != null ? ((Number) req.get("pharmacyCharge")).doubleValue() : 0.0);
            bill.setOtherCharges(req.get("otherCharges") != null ? ((Number) req.get("otherCharges")).doubleValue() : 0.0);

            double total = bill.getConsultationCharge() + bill.getRoomCharge() +
                    bill.getLabCharge() + bill.getPharmacyCharge() + bill.getOtherCharges();
            bill.setTotalAmount(total);
            bill.setNotes((String) req.get("notes"));
            bill.setStatus(Bill.BillStatus.PENDING);

            Bill saved = billRepo.save(bill);
            response.put("message", "Bill generated successfully");
            response.put("billId", saved.getId());
            response.put("totalAmount", total);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Bill>> getPatientBills(@PathVariable Long patientId) {
        return ResponseEntity.ok(billRepo.findByPatientId(patientId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billRepo.findAll());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Bill>> getPendingBills() {
        return ResponseEntity.ok(billRepo.findByStatus(Bill.BillStatus.PENDING));
    }

    @PutMapping("/pay/{billId}")
    public ResponseEntity<Map<String, String>> payBill(
            @PathVariable Long billId,
            @RequestBody Map<String, Object> req) {
        Map<String, String> response = new HashMap<>();
        return billRepo.findById(billId).map(bill -> {
            double amount = req.get("amount") != null ? ((Number) req.get("amount")).doubleValue() : bill.getTotalAmount();
            bill.setPaidAmount(amount);
            bill.setStatus(amount >= bill.getTotalAmount() ? Bill.BillStatus.PAID : Bill.BillStatus.PARTIALLY_PAID);
            bill.setPaidAt(LocalDateTime.now());
            billRepo.save(bill);
            response.put("message", "Payment recorded successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Bill not found");
            return ResponseEntity.badRequest().body(response);
        });
    }
}