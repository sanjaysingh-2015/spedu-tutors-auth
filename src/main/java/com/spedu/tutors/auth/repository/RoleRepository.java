package com.spedu.tutors.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<com.spedu.tutors.auth.entity.Role, Long> {
    Optional<com.spedu.tutors.auth.entity.Role> findByCode(String code);
    Optional<com.spedu.tutors.auth.entity.Role> findByName(String name);
}
