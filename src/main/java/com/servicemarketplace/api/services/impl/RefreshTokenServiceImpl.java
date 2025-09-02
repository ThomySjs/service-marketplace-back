package com.servicemarketplace.api.services.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.servicemarketplace.api.config.JwtUtils;
import com.servicemarketplace.api.config.TokenTypes;
import com.servicemarketplace.api.domain.entities.RefreshToken;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.RefreshTokenRepository;
import com.servicemarketplace.api.domain.repositories.UserRepository;
import com.servicemarketplace.api.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public RefreshToken createToken(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return null;
        }

        RefreshToken token = RefreshToken.builder()
            .token(jwtUtils.generateToken(email, TokenTypes.REFRESH))
            .user(user.get())
            .build();

        return refreshTokenRepository.save(token);
    }

    @Override
    public boolean isRevoked(String token){
        RefreshToken foundToken = refreshTokenRepository.findByToken(token);

        return foundToken.isRevoked();
    }

    @Override
    public boolean isExpired(String token) {
        return jwtUtils.getExpirationFromToken(token).before(new Date());
    };

    @Override
    public void revokeToken(String token) {
        RefreshToken foundToken = refreshTokenRepository.findByToken(token);
        foundToken.setRevoked(true);

        refreshTokenRepository.save(foundToken);
    };
}
