package com.servicemarketplace.api.dto.auth;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no debe estar vacio")
    String name,
    MultipartFile image,
    @NotNull(message = "El correo no puede ser nulo")
    @Email(message = "Email invalido")
    @NotBlank(message = "El email no puede estar vacio")
    String email,
    @NotNull
    @Size(min = 8, message = "La contraseña debe tener como minimo 8 caracteres.")
    @NotBlank(message = "La contraseña no debe estar vacia")
    String password,
    String phone,
    @NotNull(message = "El usuario debe pertenecer a una zona")
    Long zoneId,
    String address
) {
}
