package com.servicemarketplace.api.services.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.servicemarketplace.api.config.CustomConfig.MpConfig;
import com.servicemarketplace.api.services.SubscriptionService;
import com.servicemarketplace.api.services.WebhookService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final ObjectMapper objectMapper;
    private final MpConfig mpConfig;
    private final TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
    private final SubscriptionService subscriptionService;

    @Override
    public Map<String, Object> parseRequestBodyFromRequest(HttpServletRequest request) {
        try {
            String body = new String(request.getInputStream().readAllBytes());
            return objectMapper.readValue(body, typeRef);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public boolean validateMercadoPagoSignature(Map<String, Object> body, String signature, String requestId) {

        if (signature == null) {
            throw new IllegalArgumentException("signature invalido");
        }

        try {

            //Parsea la firma
            String ts = null;
            String v1 = null;
            String[] parts = signature.split(",");

            for (String part : parts) {
                String[] keyValue = part.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    if (key.equals("ts")) {
                        ts = value;
                    } else if (key.equals("v1")) {
                        v1 = value;
                    }
                }
            }
            Object data = body.get("data");
            if (ts == null || v1 == null || data == null) {
                throw new Exception("Firma invalida.");
            }

            Map<String, String> mappedData = objectMapper.convertValue(data, new TypeReference<Map<String, String>>() {});
            String id = mappedData.get("id");

            String template = String.format("id:%s;request-id:%s;ts:%s;", id, requestId, ts);

            String cyphedSignature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, mpConfig.getSignature()).hmacHex(template);

            if (cyphedSignature.equals(v1)) {
                return true;
            }

            return false;
        }catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    @Override
    public void mercadoPagoWebhook(Map<String, Object> body) {
        Object data = body.get("data");
        Map<String, String> mappedData = objectMapper.convertValue(data, new TypeReference<Map<String, String>>() {});

        try {
            String type = body.get("type").toString();

            if (type.equals("subscription_preapproval")){
                subscriptionService.handlePreapproval(mappedData);
            }
            else if (type.equals("payment")){
                subscriptionService.handlePayment(mappedData);
            }

        }catch (MPApiException e) {
            System.out.println("MPApiException: " + e.getApiResponse().getContent());
        }catch (MPException e) {
            System.out.println("MPException: " + e);
        }
    }
}
