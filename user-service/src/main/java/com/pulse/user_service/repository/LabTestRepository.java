package com.pulse.user_service.repository;

import com.pulse.user_service.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    List<LabTest> findByPatientId(Long patientId);
    List<LabTest> findByStatus(LabTest.TestStatus status);
    List<LabTest> findByLabTechId(Long labTechId);
}