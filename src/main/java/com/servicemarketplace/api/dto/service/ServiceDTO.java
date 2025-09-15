package com.servicemarketplace.api.dto.service;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ServiceDTO(
    Long id,
    @NotBlank(message = "El servicio debe pertenecer a una categoria.")
    Long categoryId,
    MultipartFile image,
    @NotBlank(message = "El servicio debe tener un titulo.")
    @Size(min = 10, message = "El titulo debe tener al menos 10 caracteres.")
    String title,
    String description,
    Double price
) {
}
