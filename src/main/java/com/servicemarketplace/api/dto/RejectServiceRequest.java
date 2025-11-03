package com.servicemarketplace.api.dto;

import jakarta.validation.constraints.NotNull;

public record RejectServiceRequest(
    @NotNull(message = "serviceRejectCauseId es obligatorio")
    Long serviceRejectCauseId
) {}

