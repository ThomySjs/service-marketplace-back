package com.servicemarketplace.api.dto.transaction;

import java.time.LocalDateTime;

import com.servicemarketplace.api.domain.entities.Membership;
import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.user.UserForTransactionDTO;
import com.servicemarketplace.api.mappers.UserMapper;

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
    private UserForTransactionDTO user;
    private Membership membership;
    private String state;
    private LocalDateTime date;

    public void fromTransaction(Transaction transaction, Membership membership, User user) {
        this.id = transaction.getId();
        this.user = UserMapper.toUserForTransactionDTO(user);
        this.membership = membership;
        this.state = transaction.getState();
        this.date = transaction.getDate();
    }
}
