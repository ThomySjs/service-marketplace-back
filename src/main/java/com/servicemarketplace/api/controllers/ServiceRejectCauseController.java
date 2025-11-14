package com.servicemarketplace.api.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.domain.entities.ServiceRejectCause;
import com.servicemarketplace.api.dto.RejectCauseDTO;
import com.servicemarketplace.api.dto.ServiceRejectCauseDTO;
import com.servicemarketplace.api.services.ServiceRejectCauseService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/service-reject-causes")
public class ServiceRejectCauseController {

    private final ServiceRejectCauseService service;

    @Operation(summary = "Obtiene todas las causas de rechazo de servicio.")
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ServiceRejectCauseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Obtiene todas las causas de rechazo de servicio.")
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginated")
    public ResponseEntity<Page<ServiceRejectCause>> getAllPaginated(Pageable pageable) {
        return ResponseEntity.ok(service.getAllPaginated(pageable));
    }

    @Operation(summary = "Agrega un motivo de rechazo.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Validated @RequestBody RejectCauseDTO request) {
        service.create(request.getMessage());
        return ResponseEntity.ok("Servicio creado correctamente");
    }

    @Operation(summary = "Elimina un motivo.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long causeId) {
        service.delete(causeId);
        return ResponseEntity.ok("Servicio eliminado correctamente");
    }
}

