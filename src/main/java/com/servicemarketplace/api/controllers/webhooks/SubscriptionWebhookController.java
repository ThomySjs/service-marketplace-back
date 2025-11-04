package com.servicemarketplace.api.controllers.webhooks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.services.WebhookService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks/subscriptions")
public class SubscriptionWebhookController {

    private final WebhookService webhookService;


    @PostMapping()
    public ResponseEntity<String> getMercadoPagoWebhook(
        @RequestHeader(name = "X-Signature", required = false) String signature,
        @RequestHeader(name = "X-Request-Id", required = false) String requestId,
        HttpServletRequest request) throws IOException
    {
        Map<String, Object> body = webhookService.parseRequestBodyFromRequest(request);
        if (!webhookService.validateMercadoPagoSignature(body, signature, requestId)) {
            return ResponseEntity.badRequest().body("Firma invalida");
        };

        webhookService.mercadoPagoWebhook(body);
        return ResponseEntity.ok("OK");
    }
}
