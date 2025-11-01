package com.servicemarketplace.api.services;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public interface WebhookService {

    public Map<String, Object> parseRequestBodyFromRequest(HttpServletRequest request);

    public boolean validateMercadoPagoSignature(Map<String, Object> body, String signature, String requestId);

    public void mercadoPagoWebhook(Map<String, Object> body);
}
