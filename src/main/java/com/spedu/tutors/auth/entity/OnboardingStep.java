package com.spedu.tutors.auth.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "onboarding_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "onboarding_type")
    private String onboardingType;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name="order_no")
    private Integer orderNo;
    @Column(name = "status")
    private String status;
    @Column(nullable = false, updatable = false)
    private Long createdBy;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
