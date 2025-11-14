package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.transaction.TransactionDTO;
import com.servicemarketplace.api.dto.transaction.TransactionResponseDTO;
import com.servicemarketplace.api.services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/admin/transactions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll(@RequestParam(value = "id", required = false) Long id, Pageable pageable) {
        if (id != null) {
            return ResponseEntity.ok(transactionService.getById(id));
        }

        return ResponseEntity.ok(transactionService.getAll(pageable));
    }

    @PostMapping("/admin/transactions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TransactionResponseDTO> create(@Valid @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionService.create(dto));
    }

}
