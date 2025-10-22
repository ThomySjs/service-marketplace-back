package com.servicemarketplace.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servicemarketplace.api.domain.entities.RecoveryCode;

public interface RecoveryCodeRepository extends JpaRepository<RecoveryCode, Long> {

    public Optional<RecoveryCode> findByEmail(String email);
}
