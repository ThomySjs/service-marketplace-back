package com.servicemarketplace.api.config;

public enum TokenTypes {
    //Enum utilizado para centralizar los tipos de token y sus expiraciones.
    SESSION("Session", 86400000),
    CONFIRMATION("Confirmation", 1800000),
    REVOKED("Revoked", 1);

    private final String type;
    private final Integer expTime;

    TokenTypes(String type, Integer expTime) {
        this.type = type;
        this.expTime = expTime;
    }

    public String getType() {
        return type ;
    }
    public Integer getExpTime() {
        return expTime;
    }
}
