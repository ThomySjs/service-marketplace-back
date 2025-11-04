package com.servicemarketplace.api.dto.membership;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MembershipDTO(
    @NotNull(message = "El nombre no puede ser nulo.")
    @NotBlank(message = "El nombre no puede estar vacio.")
    String name,
    @NotNull(message = "El precio no puede ser nulo.")
    Double price,
    String description
) {}
