package com.servicemarketplace.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import com.servicemarketplace.api.config.CustomConfig.UrlConfig;
import com.servicemarketplace.api.dto.auth.ChangePasswordDTO;
import com.servicemarketplace.api.dto.auth.LoginRequest;
import com.servicemarketplace.api.dto.auth.RecoverPasswordDTO;
import com.servicemarketplace.api.dto.auth.RecoveryCodeDTO;
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

    private final UrlConfig urlConfig;
    private final AuthService authService;

    @Operation(summary = "Registra un usuario.")
    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<String> register(@Valid @ModelAttribute RegisterRequest request) {
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

    @Operation(summary = "Valida las credenciales del admin y genera tokens de acceso si tiene rol ADMIN.")
    @PostMapping("/admin/login")
    @ResponseBody
    public ResponseEntity<TokenResponse> adminLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginAdmin(request));
    }

    @Operation(summary = "Verifica el correo electronico del usuario.")
    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token, Model model) {
        Map<?, ?> response = authService.verify(token);

        System.out.println(urlConfig.getFrontendUrl());

        model.addAttribute("message", response.get("message"));
        model.addAttribute("status", response.get("status"));
        model.addAttribute("redirectUrl", urlConfig.getFrontendUrl() + "login");
        return "email-confirmation";
    }

    @Operation(summary = "Actualiza la contraseña de un usuario.")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        authService.changePassword(dto);
        return ResponseEntity.ok("Contraseña actualizada con exito.");
    }

    @Operation(summary = "Envia codigo de recuperacion de contraseña.")
    @GetMapping("/recovery-code")
    public ResponseEntity<String> sendRecoveryCode(@RequestParam("email") String email) {
        if (email != null) {
            authService.sendRecoveryCode(email);
            return ResponseEntity.ok("Codigo enviado, revisa tu casilla de correo electronico.");
        }

        return ResponseEntity.badRequest().body("Correo invalido.");
    }

    @Operation(summary = "Valida el codigo de recuperacion.")
    @PostMapping("/recovery-code")
    public ResponseEntity<?> validateRecoveryCode(@Valid @RequestBody RecoveryCodeDTO code) {
        return ResponseEntity.ok(authService.validateRecoveryCode(code));
    }

    @Operation(summary = "Actualiza la contraseña de un usuario.")
    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String header, @RequestBody RecoverPasswordDTO password) {
        authService.recoverPassword(header, password.password());
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
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
