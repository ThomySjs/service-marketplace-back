package com.servicemarketplace.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServiceRequest(
    @NotBlank(message = "El servicio debe pertenecer a una categoria.")
    Long categoryId,
    @NotBlank(message = "El servicio debe tener un titulo.")
    @Size(min = 10, message = "El titulo debe tener al menos 10 caracteres.")
    String title,
    String description,
    Double price
) {
}
