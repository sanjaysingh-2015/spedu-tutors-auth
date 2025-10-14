package com.spedu.tutors.auth.repository;

import com.spedu.tutors.auth.entity.TutorOnboardingStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TutorOnboardingStepRepository extends JpaRepository<TutorOnboardingStep, Long> {
    List<TutorOnboardingStep> findByUserId(Long userId);
    Optional<TutorOnboardingStep> findByUserIdAndStatus(Long userId, String status);
    Optional<TutorOnboardingStep> findByUserIdAndOnboardingStepCode(Long userId, String stepCode);
    boolean existsByUserIdAndStatusNot(Long userId, String status);
}