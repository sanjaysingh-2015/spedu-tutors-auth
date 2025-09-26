package com.spedu.tutors.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="blacklisted_tokens")
public class BlacklistedTokenEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This tells JPA to use MySQL AUTO_INCREMENT
    private Long id;
    private String token;
    private LocalDateTime expiryTime;
}
