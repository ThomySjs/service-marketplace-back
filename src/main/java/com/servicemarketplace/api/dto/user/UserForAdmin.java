package com.servicemarketplace.api.dto.user;

import java.time.LocalDateTime;

public record UserForAdmin(
    Long id,
    String name,
    String email,
    String address,
    String phone,
    String role,
    boolean verified,
    LocalDateTime createdAt
) {
}
