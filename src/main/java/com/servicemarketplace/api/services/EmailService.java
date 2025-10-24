package com.servicemarketplace.api.services;


public interface EmailService {

    public boolean validateAddress(String email);

    public void sendConfirmationEmail(String to, String appUrl, String username);

    public void sendRecoveryCode(String to, int code, String expiration);
}
