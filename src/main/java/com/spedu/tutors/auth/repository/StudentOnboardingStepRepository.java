package com.spedu.tutors.auth.repository;

import com.spedu.tutors.auth.entity.StudentOnboardingStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentOnboardingStepRepository extends JpaRepository<StudentOnboardingStep, Long> {
    List<StudentOnboardingStep> findByUserId(Long userId);
    Optional<StudentOnboardingStep> findByUserIdAndStatus(Long userId, String status);
    Optional<StudentOnboardingStep> findByUserIdAndOnboardingStepCode(Long userId, String stepCode);
    boolean existsByUserIdAndStatusNot(Long userId, String status);
}