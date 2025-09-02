package com.servicemarketplace.api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public RefreshToken findByToken(String token);

    public RefreshToken findBySession(String session);
}
