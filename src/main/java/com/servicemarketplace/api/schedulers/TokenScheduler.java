package com.servicemarketplace.api.schedulers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.servicemarketplace.api.config.TokenTypes;
import com.servicemarketplace.api.domain.entities.RefreshToken;
import com.servicemarketplace.api.domain.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 */15 * * * *")
    public void revoke() {
        System.out.println("Corriendo crono revoke");
        List<RefreshToken> tokens = refreshTokenRepository.findAll();
        for (RefreshToken rt : tokens) {
            if (rt.getCreatedAt().plusSeconds(TokenTypes.REFRESH.getExpTime()).isBefore(LocalDateTime.now()) || rt.isRevoked()) {
                refreshTokenRepository.delete(rt);
            }
        }
    }
}
