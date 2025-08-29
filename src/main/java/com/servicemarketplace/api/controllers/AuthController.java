package com.servicemarketplace.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.auth.RegisterRequest;
import com.servicemarketplace.api.dto.auth.RegisterResponse;
import com.servicemarketplace.api.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        if (response.status().startsWith("error")) {
            return ResponseEntity.badRequest().body(response.message());
        }
        return ResponseEntity.ok(response.message());
    }

}
