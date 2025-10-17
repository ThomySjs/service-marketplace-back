package com.servicemarketplace.api.dto.user;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateUserDTO(
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no debe estar vacio")
    String name,
    String address,
    String phone,
    MultipartFile profilePicture
) {
}
