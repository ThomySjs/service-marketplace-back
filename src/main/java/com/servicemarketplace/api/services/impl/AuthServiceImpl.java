package com.servicemarketplace.api.services.impl;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servicemarketplace.api.config.JwtUtils;
import com.servicemarketplace.api.config.TokenTypes;
import com.servicemarketplace.api.config.CustomConfig.MailConfig;
import com.servicemarketplace.api.domain.entities.RecoveryCode;
import com.servicemarketplace.api.domain.entities.RefreshToken;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.RecoveryCodeRepository;
import com.servicemarketplace.api.domain.repositories.UserRepository;
import com.servicemarketplace.api.dto.auth.ChangePasswordDTO;
import com.servicemarketplace.api.dto.auth.LoginRequest;
import com.servicemarketplace.api.dto.auth.RecoveryCodeDTO;
import com.servicemarketplace.api.dto.auth.RegisterRequest;
import com.servicemarketplace.api.dto.auth.RegisterResponse;
import com.servicemarketplace.api.dto.auth.TokenResponse;
import com.servicemarketplace.api.exceptions.auth.ResourceNotFoundException;
import com.servicemarketplace.api.exceptions.auth.UserNotVerifiedException;
import com.servicemarketplace.api.services.AuthService;
import com.servicemarketplace.api.services.EmailService;
import com.servicemarketplace.api.services.ImageService;
import com.servicemarketplace.api.services.UserService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RouteService routeService;
    private final MailConfig mailConfig;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final ImageService imageService;
    private final RecoveryCodeRepository recoveryCodeRepository;


    @Override
    public RegisterResponse register(RegisterRequest request) {
        User newUser = User.builder()
            .name(request.name())
            .email(request.email())
            .imagePath(imageService.upload(request.image()))
            .phone(request.phone())
            .password(passwordEncoder.encode(request.password()))
            .address(request.address())
            .zoneId(request.zoneId())
            .build();

        try {
            User savedUser = userRepository.save(newUser);
            if (mailConfig.isEnabled()) {
                mailService.sendConfirmationEmail(savedUser.getEmail(), routeService.getAppUrl(), savedUser.getName());
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

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }

        User foundUser = user.get();
        String sessionToken = jwtUtils.generateSessionToken(
            email,
            refreshToken.getSession(),
            Map.of("name", foundUser.getName(), "role", foundUser.getRole())
        );

        return new TokenResponse(sessionToken, refreshToken.getToken());
    }

    @Override
    public TokenResponse login(LoginRequest request){
        //Autentica al usuario y lo trae de la base de datos
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userRepository.findByEmail(request.email()).get();

        //Verifica si el usuario confirmo su correo y si la opcion de correos esta habilitada (Solo para desarrollo)
        if (!user.isVerified() && mailConfig.isEnabled()) {
            mailService.sendConfirmationEmail(user.getEmail(), routeService.getAppUrl(), user.getName());
            throw new UserNotVerifiedException("Correo electronico no confirmado.");
        }

        RefreshToken refreshToken = refreshTokenServiceImpl.createToken(user.getEmail());
        String sessionToken = jwtUtils.generateSessionToken(
            user.getEmail(),
            refreshToken.getSession(),
            Map.of("name", user.getName(), "role", user.getRole())
            );


        return new TokenResponse(sessionToken, refreshToken.getToken());
    }

    @Override
    public Map<?, ?> verify(String token) {
        //Parsea el token, comprueba su expiracion y verifica el tipo de token
        try {
            jwtUtils.validateToken(token);
        }catch (JwtException e){
            return Map.of(
                "message", "Token invalido.",
                "status", 400
            );
        }

        if (!jwtUtils.getTokenType(token).equals(TokenTypes.CONFIRMATION.getType())) {
            return Map.of(
                "message", "Token invalido.",
                "status", 400
            );
        }
        //Obtiene el mail del token y trae el usuario de la base de datos
        String email = jwtUtils.getUserFromToken(token);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return Map.of(
                "message", "Usuario no encontrado.",
                "status", 404
            );
        }
        User foundUser = user.get();

        //Chequea si no esta verificado
        if (foundUser.isVerified()) {
            return Map.of(
                "message", "El correo ya se encuentra verificado.",
                "status", 200
            );
        }

        //Verifica al usuario y lo persiste
        foundUser.setVerified(true);
        userRepository.save(foundUser);
        return Map.of(
                "message", "Correo verificado con éxito.",
                "status", 200
        );
    }

    @Override
    public void changePassword(ChangePasswordDTO dto) {
        //Obtiene el usuario del token
        User user = userService.getUserFromContext();

        //Compara la contraseña actual con la que esta persistida en la base de datos, si no es la misma, devuelve error
        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Contraseña invalida.");
        }

        //Si coinciden, persiste la contraseña nueva
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void sendRecoveryCode(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return;
        }

        //Verificar si ya habia un codigo y lo elimina
        Optional<RecoveryCode> oldRecoveryCode = recoveryCodeRepository.findByEmail(email);
        if (oldRecoveryCode.isPresent()) {
            recoveryCodeRepository.delete(oldRecoveryCode.get());
        }

        RecoveryCode recoveryCode = new RecoveryCode();
        recoveryCode.setEmail(email);
        RecoveryCode persistedCode = recoveryCodeRepository.save(recoveryCode);
        mailService.sendRecoveryCode(email, persistedCode.getCode(), "5");
    }

    @Override
    public Map<String, String> validateRecoveryCode(RecoveryCodeDTO dto) {
        Optional<RecoveryCode> code = recoveryCodeRepository.findByEmail(dto.email());
        if (code.isEmpty() || code.get().isExpired() || !code.get().isValid(dto)) {
            throw new IllegalArgumentException("Codigo invalido.");
        }
        recoveryCodeRepository.delete(code.get());

        Map<String, String> responseToken = Map.of("token", jwtUtils.generateToken(dto.email(), TokenTypes.RECOVERY));
        return responseToken;
    }

    @Override
    public void recoverPassword(String header, String password) {
        String jwt = header;
        //Parsea el header si contiene bearer
        if (header.contains("Bearer")) {
            jwt = jwtUtils.parseJwtFromString(header);
        }
        //Validaciones
        if (!jwtUtils.getTokenType(jwt).equals(TokenTypes.RECOVERY.getType())) {
            throw new IllegalArgumentException("Token invalido");
        }
        if (jwtUtils.getExpirationFromToken(jwt).before(new Date())) {
            throw new IllegalArgumentException("Token expirado.");
        }
        String email = jwtUtils.getUserFromToken(jwt);
        User user = userService.getUserByEmail(email);

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

}
