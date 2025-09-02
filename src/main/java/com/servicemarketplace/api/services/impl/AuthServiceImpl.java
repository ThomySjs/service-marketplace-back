package com.servicemarketplace.api.services.impl;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servicemarketplace.api.config.JwtUtils;
import com.servicemarketplace.api.config.TokenTypes;
import com.servicemarketplace.api.config.CustomConfig.MailConfig;
import com.servicemarketplace.api.domain.entities.RefreshToken;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.UserRepository;
import com.servicemarketplace.api.dto.auth.RegisterRequest;
import com.servicemarketplace.api.dto.auth.RegisterResponse;
import com.servicemarketplace.api.dto.auth.TokenResponse;
import com.servicemarketplace.api.services.AuthService;
import com.servicemarketplace.api.services.EmailService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RouteService routeService;
    private final MailConfig mailConfig;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;


    @Override
    public RegisterResponse register(RegisterRequest request) {

        User newUser = User.builder()
            .name(request.name())
            .email(request.email())
            .phone(request.phone())
            .password(passwordEncoder.encode(request.password()))
            .address(request.address())
            .createdAt(new Date())
            .zoneId(request.zoneId())
            .token(jwtUtils.generateToken(request.email(), TokenTypes.SESSION))
            .build();

        try {
            User savedUser = userRepository.save(newUser);
            if (mailConfig.isEnabled()) {
                mailService.sendConfirmationEmail(savedUser.getEmail(), routeService.getAppUrl());
            }
            return new RegisterResponse("ok", "Usuario registrado correctamente, revisa tu casilla de correo.");
        }catch (DataIntegrityViolationException e) {
            return new RegisterResponse("error", "El email se encuentra en uso, prueba con otro.");
        }catch (Exception e) {
            return new RegisterResponse("error", "Hubo un error al agregar el usuario");
        }
    }

    @Override
    public TokenResponse refresh(String authHeader) {
        final String token = jwtUtils.parseJwtFromString(authHeader);
        if (token == null || !jwtUtils.getTokenType(token).equals(TokenTypes.REFRESH.getType()) || refreshTokenServiceImpl.isRevoked(token)) {
            throw new JwtException("Refresh token invalido");
        }

        //Obtiene el mail y revoca el token utilizado
        String email = jwtUtils.getUserFromToken(token);
        refreshTokenServiceImpl.revokeToken(token);

        //Crea los tokens
        RefreshToken refreshToken = refreshTokenServiceImpl.createToken(email);
        if (refreshToken == null) {
            throw new JwtException("Refresh token invalido");
        }
        String sessionToken = jwtUtils.generateSessionToken(email, refreshToken.getSession());

        return new TokenResponse(sessionToken, refreshToken.getToken());
    }

}
