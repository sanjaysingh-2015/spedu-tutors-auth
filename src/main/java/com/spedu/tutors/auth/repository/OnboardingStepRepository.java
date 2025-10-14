package com.spedu.tutors.auth.repository;

import com.spedu.tutors.auth.entity.OnboardingStep;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OnboardingStepRepository extends JpaRepository<OnboardingStep, Long> {
    Optional<OnboardingStep> findByCode(String code);
    Optional<OnboardingStep> findByCodeAndOnboardingType(String code, String type);
    List<OnboardingStep> findAllByStatus(String status);
    List<OnboardingStep> findAllByStatusAndOnboardingType(String status, String type);
    @Query(value = "SELECT *\n" +
            "FROM onboarding_steps\n" +
            "WHERE onboarding_step = :roleCode AND status = 'ACTIVE' AND order_no = (SELECT MIN(order_no) FROM onboarding_steps AND status = 'ACTIVE')", nativeQuery = true)
    Optional<OnboardingStep> findByFirstStep(@Param("roleCode") String roleCode);
    @Query(value = "SELECT *\n" +
            "FROM onboarding_steps\n" +
            "WHERE onboarding_step = :roleCode AND status = 'ACTIVE' AND order_no = :stepId", nativeQuery = true)
    Optional<OnboardingStep> findByNextStep(@Param("roleCode") String roleCode,@Param("stepId") Integer stepId);
}