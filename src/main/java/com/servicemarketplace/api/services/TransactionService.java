package com.servicemarketplace.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.dto.transaction.TransactionDTO;
import com.servicemarketplace.api.dto.transaction.TransactionResponseDTO;

public interface TransactionService {

    public TransactionResponseDTO create(TransactionDTO dto);

    public Page<TransactionResponseDTO> getAll(Pageable pageable);

    public Transaction getById(Long id);
}