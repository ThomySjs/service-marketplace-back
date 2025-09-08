package com.servicemarketplace.api.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record UserDTO(
    Long id,
    String name,
    String email,
    String address,
    String phone,
    LocalDateTime createdAt
) {
}
