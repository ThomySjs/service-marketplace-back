package com.servicemarketplace.api.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.servicemarketplace.api.config.CustomConfig.JwtConfig;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtConfig jwtConfig;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authentication");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("El token ha expirado, por favor inicie sesión nuevamente");
        } catch (Exception e) {
            throw new JwtException("Token inválido");
        }
    }

    public String getUserFromToken(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public String generateToken(String subject, TokenTypes type) {
        return Jwts.builder()
            .signWith(key)
            .subject(subject)
            .claim("type", type.getType())
            .expiration(new Date(new Date().getTime() + type.getExpTime()))
            .compact();
    }
}
