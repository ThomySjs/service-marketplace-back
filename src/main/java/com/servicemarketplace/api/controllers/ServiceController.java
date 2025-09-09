package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.ServiceDTO;
import com.servicemarketplace.api.services.ServiceService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping()
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO request) {
        return ResponseEntity.ok(serviceService.create(request));
    }

}
