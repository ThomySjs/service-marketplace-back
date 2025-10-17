package com.servicemarketplace.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
    @NotBlank(message = "La contraseña actual es obligatoria.")
    @NotNull(message = "La contraseña actual no puede ser nula.")
    @Size(min = 8, message = "Contraseña invalida.")
    String currentPassword,
    @NotBlank(message = "La contraseña nueva es obligatoria.")
    @NotNull(message = "La contraseña nueva no puede ser nula.")
    @Size(min = 8, message = "La contraseña debe tener como minimo 8 caracteres.")
    String newPassword
) {
}
