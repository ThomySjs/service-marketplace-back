package com.servicemarketplace.api.config;

public enum Roles {
    USER(1L),
    ADMIN(1000L),
    PREMIUM(3L);

    private final Long limit;

    Roles(Long limit) {
        this.limit = limit;
    }

    public Long getServiceLimit() {
        return this.limit;
    }
}
