package com.servicemarketplace.api.config;

public enum TokenTypes {
    //Enum utilizado para centralizar los tipos de token y sus expiraciones.
    SESSION("Session", 7200000L), //2 hs
    CONFIRMATION("Confirmation", 1800000L), //30 mins
    REFRESH("Refresh", 2629800000L); //1 mes

    private final String type;
    private final Long expTime;

    TokenTypes(String type, Long expTime) {
        this.type = type;
        this.expTime = expTime;
    }

    public String getType() {
        return type ;
    }
    public Long getExpTime() {
        return expTime;
    }
}
