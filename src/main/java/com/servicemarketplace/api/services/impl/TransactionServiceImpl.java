package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.servicemarketplace.api.domain.entities.Subscription;
import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.SubscriptionRepository;
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
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public TransactionResponseDTO create(TransactionDTO dto) {
        User user = userService.getUserByEmail(dto.email());

        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }

        Subscription subscription = subscriptionRepository.getByUserId(user.getId()).orElseThrow(
            () -> new ResourceNotFoundException("El usuario no posee subscripción registrada, para crear una transacción primero debes registrar una suscripción al usuario.")
        );


        Transaction transaction = new Transaction();
        transaction.setTotal((dto.total() == null) ? subscription.getMembership().getPrice() : dto.total());
        transaction.setSubscription(subscription);

        transactionRepository.save(transaction);

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.fromTransaction(transaction);

        return transactionResponseDTO;
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