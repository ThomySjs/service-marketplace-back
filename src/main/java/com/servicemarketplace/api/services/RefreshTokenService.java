package com.servicemarketplace.api.services;

import com.servicemarketplace.api.domain.entities.RefreshToken;

public interface RefreshTokenService {
    public RefreshToken createToken(String email);

    public boolean isRevoked(String token);

    public boolean isExpired(String token);

    public void revokeToken(String token);
}
