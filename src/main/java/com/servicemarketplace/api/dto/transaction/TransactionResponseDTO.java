package com.servicemarketplace.api.dto.transaction;

import java.time.LocalDateTime;

import com.servicemarketplace.api.domain.entities.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {

    private Long id;
    private String state;
    private SubscriptionResponseDTO subscription;
    private Long mpId;
    private Double total;
    private LocalDateTime date;

    public void fromTransaction(Transaction savedTransaction) {

        SubscriptionResponseDTO subscriptionResponseDTO = new SubscriptionResponseDTO();
        subscriptionResponseDTO.fromSubscription(savedTransaction.getSubscription());
        this.id = savedTransaction.getId();
        this.state = savedTransaction.getState();
        this.subscription = subscriptionResponseDTO;
        this.mpId = savedTransaction.getMpId();
        this.total = savedTransaction.getTotal();
        this.date = savedTransaction.getDate();
    }
}
