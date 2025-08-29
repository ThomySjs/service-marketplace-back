package com.servicemarketplace.api.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("username")
    String username
) {}
