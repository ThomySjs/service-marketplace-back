package com.servicemarketplace.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.ServiceRejectCauseDTO;
import com.servicemarketplace.api.services.ServiceRejectCauseService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-reject-causes")
public class ServiceRejectCauseController {

    private final ServiceRejectCauseService service;

    @Operation(summary = "Obtiene todas las causas de rechazo de servicio.")
	 @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ServiceRejectCauseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}

