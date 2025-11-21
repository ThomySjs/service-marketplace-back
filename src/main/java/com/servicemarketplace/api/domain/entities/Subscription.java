package com.servicemarketplace.api.domain.entities;

import java.time.LocalDateTime;

import com.servicemarketplace.api.config.SubscriptionState;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull (message = "El usuario no puede ser null")
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    @ManyToOne
    @NotNull (message = "La membresía no puede ser nula")
    private Membership membership;
    @Builder.Default
    private String state = SubscriptionState.INACTIVE.name();
    @Column(columnDefinition = "TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime endDate;

}
