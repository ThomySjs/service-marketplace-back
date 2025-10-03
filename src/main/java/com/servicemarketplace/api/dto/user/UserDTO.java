package com.servicemarketplace.api.dto.user;

import java.time.LocalDateTime;
import java.util.List;

import com.servicemarketplace.api.dto.service.ServiceListResponse;

import lombok.Builder;

@Builder
public record UserDTO(
    Long id,
    String name,
    String imagePath,
    String email,
    String address,
    String phone,
    LocalDateTime createdAt,
    List<ServiceListResponse> createdServices
) {
}
