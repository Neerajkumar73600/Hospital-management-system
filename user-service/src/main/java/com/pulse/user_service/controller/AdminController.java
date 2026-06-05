package com.pulse.user_service.controller;

import com.pulse.user_service.entity.User;
import com.pulse.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepo;

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    // GET pending doctors
    @GetMapping("/pending-doctors")
    public ResponseEntity<List<User>> getPendingDoctors() {
        return ResponseEntity.ok(
            userRepo.findByStatus(User.AccountStatus.PENDING_APPROVAL)
        );
    }

    // PUT approve doctor
    @PutMapping("/approve/{userId}")
    public ResponseEntity<Map<String, String>> approveDoctor(@PathVariable Long userId) {
        Map<String, String> response = new HashMap<>();
        return userRepo.findById(userId).map(user -> {
            user.setStatus(User.AccountStatus.ACTIVE);
            userRepo.save(user);
            response.put("message", "Doctor approved successfully");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        });
    }

    // PUT suspend user
    @PutMapping("/suspend/{userId}")
    public ResponseEntity<Map<String, String>> suspendUser(@PathVariable Long userId) {
        Map<String, String> response = new HashMap<>();
        return userRepo.findById(userId).map(user -> {
            user.setStatus(User.AccountStatus.SUSPENDED);
            userRepo.save(user);
            response.put("message", "User suspended");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        });
    }

    // GET stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        List<User> all = userRepo.findAll();
        stats.put("total", (long) all.size());
        stats.put("doctors", all.stream().filter(u -> u.getRole() == User.Role.DOCTOR).count());
        stats.put("patients", all.stream().filter(u -> u.getRole() == User.Role.PATIENT).count());
        stats.put("labTechs", all.stream().filter(u -> u.getRole() == User.Role.LAB_TECH).count());
        stats.put("pending", all.stream().filter(u -> u.getStatus() == User.AccountStatus.PENDING_APPROVAL).count());
        return ResponseEntity.ok(stats);
    }
}
