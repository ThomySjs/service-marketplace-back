package com.servicemarketplace.api.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ServiceDTO(
    Long Id,
    @NotBlank(message = "El servicio debe pertenecer a una categoria.")
    Long categoryId,
    Long sellerId,
    @NotBlank(message = "El servicio debe tener un titulo.")
    @Size(min = 10, message = "El titulo debe tener al menos 10 caracteres.")
    String title,
    String description,
    Double price
) {
}
