package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.service.ServiceDTO;
import com.servicemarketplace.api.dto.service.ServiceListResponse;
import com.servicemarketplace.api.services.ServiceService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping()
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO request) {
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

}
