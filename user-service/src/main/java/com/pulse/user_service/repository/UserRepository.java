package com.pulse.user_service.repository;

import com.pulse.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByStatus(User.AccountStatus status);
    List<User> findByRole(User.Role role);
}