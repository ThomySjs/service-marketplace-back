package com.servicemarketplace.api.services.impl;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servicemarketplace.api.config.JwtUtils;
import com.servicemarketplace.api.config.TokenTypes;
import com.servicemarketplace.api.config.CustomConfig.MailConfig;
import com.servicemarketplace.api.domain.entities.RefreshToken;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.UserRepository;
import com.servicemarketplace.api.dto.auth.LoginRequest;
import com.servicemarketplace.api.dto.auth.RegisterRequest;
import com.servicemarketplace.api.dto.auth.RegisterResponse;
import com.servicemarketplace.api.dto.auth.TokenResponse;
import com.servicemarketplace.api.exceptions.auth.UserNotVerifiedException;
import com.servicemarketplace.api.services.AuthService;
import com.servicemarketplace.api.services.EmailService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
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
            .zoneId(request.zoneId())
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

    @Override
    public TokenResponse login(LoginRequest request){
        //Autentica al usuario y lo trae de la base de datos
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userRepository.findByEmail(request.email()).get();

        //Verifica si el usuario confirmo su correo y si la opcion de correos esta habilitada (Solo para desarrollo)
        if (!user.isVerified() && mailConfig.isEnabled()) {
            mailService.sendConfirmationEmail(user.getEmail(), routeService.getAppUrl());
            throw new UserNotVerifiedException("Correo electronico no confirmado.");
        }

        RefreshToken refreshToken = refreshTokenServiceImpl.createToken(user.getEmail());
        String sessionToken = jwtUtils.generateSessionToken(user.getEmail(), refreshToken.getSession());


        return new TokenResponse(sessionToken, refreshToken.getToken());
    }

    @Override
    public String verify(String token) {
        //Parsea el token, comprueba su expiracion y verifica el tipo de token
        jwtUtils.validateToken(token);
        if (!jwtUtils.getTokenType(token).equals(TokenTypes.CONFIRMATION.getType())) {
            throw new JwtException("Token invalido.");
        }

        //Obtiene el mail del token y trae el usuario de la base de datos
        String email = jwtUtils.getUserFromToken(token);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return "Usuario no encontrado.";
        }
        User foundUser = user.get();

        //Chequea si no esta verificado
        if (foundUser.isVerified()) {
            return "El correo ya se encuentra verificado.";
        }

        //Verifica al usuario y lo persiste
        foundUser.setVerified(true);
        userRepository.save(foundUser);
        return "Correo verificado con exito.";
    }

}
