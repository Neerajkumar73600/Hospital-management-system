package com.pulse.user_service.controller;

import com.pulse.user_service.dto.AppointmentRequest;
import com.pulse.user_service.entity.Appointment;
import com.pulse.user_service.entity.User;
import com.pulse.user_service.repository.AppointmentRepository;
import com.pulse.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentRepository appointmentRepo;
    private final UserRepository userRepo;

    // Book appointment
    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody AppointmentRequest req) {
        Map<String, Object> response = new HashMap<>();
        try {
            User patient = userRepo.findById(req.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
            User doctor = userRepo.findById(req.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

            Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(LocalDate.parse(req.getDate()))
                .appointmentTime(LocalTime.parse(req.getTime()))
                .type(Appointment.AppointmentType.valueOf(req.getType()))
                .reason(req.getReason())
                .status(Appointment.AppointmentStatus.SCHEDULED)
                .build();

            Appointment saved = appointmentRepo.save(appointment);
            response.put("message", "Appointment booked successfully");
            response.put("appointmentId", saved.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Get patient's appointments
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentRepo.findByPatientId(patientId));
    }

    // Get doctor's appointments
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentRepo.findByDoctorId(doctorId));
    }

    // Get all appointments (admin)
    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepo.findAll());
    }

    // Cancel appointment
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Map<String, String>> cancelAppointment(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        return appointmentRepo.findById(id).map(apt -> {
            apt.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentRepo.save(apt);
            response.put("message", "Appointment cancelled");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        });
    }

    // Complete appointment
    @PutMapping("/complete/{id}")
    public ResponseEntity<Map<String, String>> completeAppointment(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        return appointmentRepo.findById(id).map(apt -> {
            apt.setStatus(Appointment.AppointmentStatus.COMPLETED);
            appointmentRepo.save(apt);
            response.put("message", "Appointment marked as completed");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        });
    }

    // Get all doctors (for booking)
    @GetMapping("/doctors")
    public ResponseEntity<List<User>> getAllDoctors() {
        return ResponseEntity.ok(userRepo.findByRole(User.Role.DOCTOR));
    }
}
