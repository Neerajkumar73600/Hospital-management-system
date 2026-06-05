package com.pulse.user_service.repository;

import com.pulse.user_service.entity.RoomAllotment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomAllotmentRepository extends JpaRepository<RoomAllotment, Long> {
    List<RoomAllotment> findByPatientId(Long patientId);
    List<RoomAllotment> findByStatus(RoomAllotment.AllotmentStatus status);
}