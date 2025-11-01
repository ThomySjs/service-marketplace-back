package com.servicemarketplace.api.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.Subscription;
import com.servicemarketplace.api.dto.transaction.SubscriptionResponseDTO;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{

    @Query("""
        SELECT new com.servicemarketplace.api.dto.transaction.SubscriptionResponseDTO(
            s.id,
            new com.servicemarketplace.api.dto.user.UserForSubscriptionDTO(
                s.user.id, s.user.email, s.user.name),
            s.membership,
            s.state,
            s.endDate
        )
        FROM Subscription s
    """)
    List<SubscriptionResponseDTO> getAll();

    @Query("""
        SELECT s
        FROM Subscription s
        JOIN FETCH s.user
        JOIN FETCH s.membership
        WHERE s.user.id = :userId
    """)
    Optional<Subscription> getByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT s
        FROM Subscription s
        JOIN FETCH s.user
        JOIN FETCH s.membership
        WHERE s.user.email = :userEmail
    """)
    Optional<Subscription> getByUserEmail(@Param("userEmail") String userEmail);
}
