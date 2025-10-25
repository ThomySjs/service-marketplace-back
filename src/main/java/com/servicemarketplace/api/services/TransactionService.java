package com.servicemarketplace.api.services;

import java.util.List;

import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.dto.transaction.TransactionDTO;
import com.servicemarketplace.api.dto.transaction.TransactionResponseDTO;

public interface TransactionService {

    public TransactionResponseDTO create(TransactionDTO dto);

    public void delete(Long id);

    public List<TransactionResponseDTO> getAll();

    public Transaction getById(Long id);
}
