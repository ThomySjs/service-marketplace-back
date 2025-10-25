package com.servicemarketplace.api.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.dto.transaction.TransactionResponseDTO;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    @Query("""
        SELECT new com.servicemarketplace.api.dto.transaction.TransactionResponseDTO(
            t.id,
            new com.servicemarketplace.api.dto.user.UserForTransactionDTO(
                t.user.id, t.user.email, t.user.name),
            t.membership,
            t.state,
            t.date
        )
        FROM Transaction t
    """)
    public List<TransactionResponseDTO> getAll();
}
