package com.servicemarketplace.api.dto.transaction;

import java.time.LocalDateTime;

import com.servicemarketplace.api.domain.entities.Membership;
import com.servicemarketplace.api.domain.entities.Subscription;
import com.servicemarketplace.api.dto.user.UserForSubscriptionDTO;
import com.servicemarketplace.api.mappers.UserMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponseDTO {

    private Long id;
    private UserForSubscriptionDTO user;
    private Membership membership;
    private String state;
    private LocalDateTime endDate;

    public void fromSubscription(Subscription subscription) {
        this.id = subscription.getId();
        this.user = UserMapper.toUserForSubscriptionDTO(subscription.getUser());
        this.membership = subscription.getMembership();
        this.state = subscription.getState();
        this.endDate = subscription.getEndDate();
    }
}
