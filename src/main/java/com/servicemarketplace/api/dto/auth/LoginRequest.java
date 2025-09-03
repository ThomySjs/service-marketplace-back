package com.servicemarketplace.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @Email(message = "Correo invalido.")
    String email,
    @NotBlank(message = "Contraseña invalida.")
    String password
) {

}
