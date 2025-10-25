package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.servicemarketplace.api.domain.entities.Membership;
import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.MembershipRepository;
import com.servicemarketplace.api.domain.repositories.TransactionRepository;
import com.servicemarketplace.api.dto.transaction.TransactionDTO;
import com.servicemarketplace.api.dto.transaction.TransactionResponseDTO;
import com.servicemarketplace.api.exceptions.auth.ResourceNotFoundException;
import com.servicemarketplace.api.services.TransactionService;
import com.servicemarketplace.api.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final MembershipRepository membershipRepository;

    @Override
    public TransactionResponseDTO create(TransactionDTO dto) {
        User user = userService.getUserByEmail(dto.email());
        Optional<Membership> membership = membershipRepository.findById(dto.membershipId());

        if (user == null || membership.isEmpty()) {
            throw new ResourceNotFoundException("Usuario o membresía no encontrado.");
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setMembership(membership.get());

        transactionRepository.save(transaction);

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.fromTransaction(transaction, membership.get(), user);

        return transactionResponseDTO;
    }

    @Override
    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<TransactionResponseDTO> getAll() {
        return transactionRepository.getAll();
    }

    @Override
    public Transaction getById(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException("No se encontro la transacción.");
        }

        return transaction.get();
    }
}
