package com.servicemarketplace.api.dto.user;

import lombok.Builder;

@Builder
public record UserForTransactionDTO(
    Long id,
    String email,
    String name
) {}
