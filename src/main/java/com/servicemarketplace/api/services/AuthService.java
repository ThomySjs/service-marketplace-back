package com.servicemarketplace.api.services;

import java.util.Map;

import com.servicemarketplace.api.dto.auth.ChangePasswordDTO;
import com.servicemarketplace.api.dto.auth.LoginRequest;
import com.servicemarketplace.api.dto.auth.RecoveryCodeDTO;
import com.servicemarketplace.api.dto.auth.RegisterRequest;
import com.servicemarketplace.api.dto.auth.RegisterResponse;
import com.servicemarketplace.api.dto.auth.TokenResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    TokenResponse refresh(String authHeader);

    TokenResponse login(LoginRequest request);

    String verify(String token);

    void changePassword(ChangePasswordDTO dto);

    void sendRecoveryCode(String email);

    Map<String, String> validateRecoveryCode(RecoveryCodeDTO dto);

    void recoverPassword(String header, String password);
}
