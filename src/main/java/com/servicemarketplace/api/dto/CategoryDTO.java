package com.servicemarketplace.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
    Long id,
    @NotBlank(message = "La categoria debe tener un titulo.")
    String title,
    String description
) {
}
