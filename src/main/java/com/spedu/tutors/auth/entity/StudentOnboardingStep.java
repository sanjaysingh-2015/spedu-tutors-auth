package com.spedu.tutors.auth.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_onboarding_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentOnboardingStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "onboarding_step_id")
    private OnboardingStep onboardingStep;
    @Column(name = "status")
    private String status;
    @Column(nullable = false, updatable = false)
    private Long createdBy;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
