package com.servicemarketplace.api.dto.auth;

import jakarta.validation.constraints.Email;

public record RecoveryCodeDTO(
    @Email(message = "Email invalido")
    String email,
    int code
) {
}
