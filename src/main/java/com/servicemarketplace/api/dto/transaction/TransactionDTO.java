package com.servicemarketplace.api.dto.transaction;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record TransactionDTO(
    @NotNull (message = "El email no puede ser nulo")
    @Email (message = "Email invalido")
    String email,
    Double total
) {}
