package com.pulse.user_service.service;

import com.pulse.user_service.dto.*;
import com.pulse.user_service.entity.User;
import com.pulse.user_service.entity.User.*;
import com.pulse.user_service.repository.UserRepository;
import com.pulse.user_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req) {

        if (userRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");

        AccountStatus status = req.getRole() == Role.DOCTOR
                ? AccountStatus.PENDING_APPROVAL
                : AccountStatus.ACTIVE;

        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .dateOfBirth(req.getDateOfBirth())
                .role(req.getRole())
                .status(status)
                .build();

        User saved = userRepo.save(user);

        String message = status == AccountStatus.PENDING_APPROVAL
                ? "Doctor account submitted. Awaiting admin approval."
                : "Account created successfully!";

        return AuthResponse.builder()
                .userId(saved.getId())
                .name(saved.getFirstName() + " " + saved.getLastName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .status(status.name())
                .message(message)
                .build();
    }

    public AuthResponse login(LoginRequest req) {

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("No account found with this email"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Incorrect password");

        if (user.getStatus() == AccountStatus.PENDING_APPROVAL)
            throw new RuntimeException("Account pending admin approval");

        if (user.getStatus() == AccountStatus.SUSPENDED)
            throw new RuntimeException("Account suspended. Contact admin.");

        String token = jwtUtil.generateToken(
                user.getId(), user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus().name())
                .message("Login successful")
                .build();
    }
}