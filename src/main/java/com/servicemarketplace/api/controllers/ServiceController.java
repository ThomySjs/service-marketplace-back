package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.service.ServiceCreatedDTO;
import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceListResponse;
import com.servicemarketplace.api.services.ServiceService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceCreatedDTO> createService(@ModelAttribute ServiceDTO request) {
        return ResponseEntity.ok(serviceService.create(request));
    }

    @GetMapping()
    public ResponseEntity<List<ServiceListResponse>> getServices(
        @RequestParam(value = "seller", required = false) Optional<Long> sellerId,
        @RequestParam(value = "category", required = false) Optional<Long> categoryId) {
        if (sellerId.isPresent()) {
            return ResponseEntity.ok(serviceService.getBySeller(sellerId.get()));
        }

        if (categoryId.isPresent()) {
            return ResponseEntity.ok(serviceService.getByCategory(categoryId.get()));
        }

        return ResponseEntity.ok(serviceService.getAllNotDeleted());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        serviceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceCreatedDTO> updateService(@ModelAttribute ServiceDTO request) {
        return ResponseEntity.ok(serviceService.update(request));
    }

}
