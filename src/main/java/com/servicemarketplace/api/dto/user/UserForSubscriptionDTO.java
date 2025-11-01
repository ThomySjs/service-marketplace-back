package com.servicemarketplace.api.dto.user;

import lombok.Builder;

@Builder
public record UserForSubscriptionDTO(
    Long id,
    String email,
    String name
) {}
