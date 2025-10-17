package com.servicemarketplace.api.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.servicemarketplace.api.config.CustomConfig.JwtConfig;
import com.servicemarketplace.api.domain.entities.RefreshToken;
import com.servicemarketplace.api.domain.repositories.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
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
    private final RefreshTokenRepository refreshTokenRepository;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public String parseJwtFromString(String jwt) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            return jwt.substring(7);
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
            throw new JwtException("token_expirado");
        } catch (Exception e) {
            throw new JwtException("token_invalido");
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

    public String getSessionFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return claims.get("session", String.class);
    }

    public Date getExpirationFromToken(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }

    public String generateToken(String subject, TokenTypes type) {
        return Jwts.builder()
            .signWith(key)
            .subject(subject)
            .issuedAt(new Date())
            .claim("type", type.getType())
            .claim("jti", UUID.randomUUID().toString())
            .expiration(new Date(new Date().getTime() + type.getExpTime()))
            .compact();
    }

    public String generateSessionToken(String subject, String session) {
        return Jwts.builder()
            .signWith(key)
            .subject(subject)
            .claim("type", TokenTypes.SESSION.getType())
            .claim("session", session)
            .expiration(new Date(new Date().getTime() + TokenTypes.SESSION.getExpTime()))
            .compact();
    }

    /**
     * Verifica que el token sea de sesion y que la sesion no este revocada.
    */
    public boolean validateSession(String token) {
        //Obtiene la session del token
        String session = getSessionFromToken(token);

        //Obtiene la entidad de refresh token para validar si la sesion esta revocada
        RefreshToken refreshToken = refreshTokenRepository.findBySession(session);
        if (!getTokenType(token).equals(TokenTypes.SESSION.getType()) || refreshToken.isRevoked()) {
            return false;
        }
        return true;
    }

    public String getTokenType(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return claims.get("type", String.class);
    }

}
