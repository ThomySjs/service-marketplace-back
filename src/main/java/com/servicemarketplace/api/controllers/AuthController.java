package com.servicemarketplace.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servicemarketplace.api.dto.auth.ChangePasswordDTO;
import com.servicemarketplace.api.dto.auth.LoginRequest;
import com.servicemarketplace.api.dto.auth.RegisterRequest;
import com.servicemarketplace.api.dto.auth.RegisterResponse;
import com.servicemarketplace.api.dto.auth.TokenResponse;
import com.servicemarketplace.api.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.ui.Model;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registra un usuario.")
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        if (response.status().startsWith("error")) {
            return ResponseEntity.badRequest().body(response.message());
        }
        return ResponseEntity.ok(response.message());
    }

    @Operation(summary = "Crea nuevos refresh y session tokens.")
    @PostMapping("/refresh")
    @ResponseBody
	public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
		final TokenResponse token = authService.refresh(authHeader);
		return ResponseEntity.ok(token);
	}

    @Operation(summary = "Valida las credenciales del usuario y genera tokens se acceso.")
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Verifica el correo electronico del usuario.")
    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token, Model model) {
        String message = authService.verify(token);

        model.addAttribute("message", message);
        return "email-confirmation";
    }

    @Operation(summary = "Actualiza la contraseña de un usuario.")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        authService.changePassword(dto);
        return ResponseEntity.ok("Contraseña actualizada con exito.");
    }

	@GetMapping("/info")
	public Map<String, Object> getPodInfo() throws UnknownHostException
	{
		Map<String, Object> info = new HashMap<>();

		// Nombre del Pod (hostname)
		String podName = InetAddress.getLocalHost().getHostName();
		info.put("podName", podName);

		// Variables de entorno
		Map<String, String> env = System.getenv();
		info.put("environmentVariables", env);

		return info;
	}
}
