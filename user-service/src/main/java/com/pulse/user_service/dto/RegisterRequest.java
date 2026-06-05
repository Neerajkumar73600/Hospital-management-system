package com.pulse.user_service.dto;

import com.pulse.user_service.entity.User;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    private LocalDate dateOfBirth;

    @NotNull(message = "Role is required")
    private User.Role role;

    // Doctor-only fields
    private String specialization;
    private String licenseNumber;
    private Integer experience;
}