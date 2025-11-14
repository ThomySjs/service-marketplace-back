package com.servicemarketplace.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.dto.transaction.TransactionResponseDTO;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT t
        FROM Transaction t
        WHERE t.mpId = :mpId
    """)
    Optional<Transaction> getByMpId(@Param("mpId") Long mpId);

    @Query("""
        SELECT new com.servicemarketplace.api.dto.transaction.TransactionResponseDTO(
            t.id,
            t.state,
            new com.servicemarketplace.api.dto.transaction.SubscriptionResponseDTO(
                t.subscription.id,
                new com.servicemarketplace.api.dto.user.UserForSubscriptionDTO(
                    t.subscription.user.id,
                    t.subscription.user.email,
                    t.subscription.user.name
                ),
                t.subscription.membership,
                t.subscription.state,
                t.subscription.endDate
            ),
            t.mpId,
            t.total,
            t.date
        )
        FROM Transaction t
    """)
    Page<TransactionResponseDTO> getAll(Pageable pageable);

}
