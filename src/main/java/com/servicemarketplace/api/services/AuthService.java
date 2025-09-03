package com.servicemarketplace.api.services;

import com.servicemarketplace.api.dto.auth.LoginRequest;
import com.servicemarketplace.api.dto.auth.RegisterRequest;
import com.servicemarketplace.api.dto.auth.RegisterResponse;
import com.servicemarketplace.api.dto.auth.TokenResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    TokenResponse refresh(String authHeader);

    TokenResponse login(LoginRequest request);
}
