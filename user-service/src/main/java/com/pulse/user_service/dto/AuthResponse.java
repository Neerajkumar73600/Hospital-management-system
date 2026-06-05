package com.pulse.user_service.dto;

import com.pulse.user_service.entity.User;
import lombok.*;

@Data @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private Long   userId;
    private String name;
    private String email;
    private User.Role role;
    private String status;
    private String message;
}