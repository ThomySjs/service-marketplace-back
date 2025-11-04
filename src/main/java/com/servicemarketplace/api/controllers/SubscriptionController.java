package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.transaction.SubscriptionDTO;
import com.servicemarketplace.api.dto.transaction.SubscriptionResponseDTO;
import com.servicemarketplace.api.services.SubscriptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return ResponseEntity.ok(subscriptionService.getById(id));
        }

        return ResponseEntity.ok(subscriptionService.getAll());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SubscriptionResponseDTO> create(@Valid @RequestBody SubscriptionDTO dto) {
        return ResponseEntity.ok(subscriptionService.create(dto));
    }

    @GetMapping("/checkout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, String>> checkout(@RequestHeader(HttpHeaders.AUTHORIZATION) String header, @RequestParam("membership") Long membershipId) {
        String link = subscriptionService.checkout(membershipId, header);
        if (link != null) {
            return ResponseEntity.ok(Map.of("link", link));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Ocurrio un error al crear el link de pago, vuelve a intentarlo mas tarde."));
    }

}
