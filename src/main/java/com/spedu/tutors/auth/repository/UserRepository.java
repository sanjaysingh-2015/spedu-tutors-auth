package com.spedu.tutors.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<com.spedu.tutors.auth.entity.User, Long> {
    Optional<com.spedu.tutors.auth.entity.User> findByEmail(String email);
    boolean existsByEmail(String email);
}
