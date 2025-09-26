package com.spedu.tutors.auth.repository;

import com.spedu.tutors.auth.entity.BlacklistedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedTokenEntity, Long> {
    Boolean existsByToken(String token);
}
