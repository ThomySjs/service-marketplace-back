package com.servicemarketplace.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecoverPasswordDTO(
    @Size(min = 8, message = "La contraseña debe tener como minimo 8 caracteres.")
    @NotBlank(message = "La contraseña no debe estar vacia")
    String password
) {}
