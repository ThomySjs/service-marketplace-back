package com.servicemarketplace.api.schedulers;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.servicemarketplace.api.domain.entities.Subscription;
import com.servicemarketplace.api.domain.repositories.SubscriptionRepository;
import com.servicemarketplace.api.services.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 * * * *")
    public void checkSubscription() {
        //Obtiene todas las suscripciones
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        //Chequea la fecha de expiracion de cada suscripcion, si es anterior a ahora cambia el rol del usuario
        for (Subscription s : subscriptions) {
            if (s.getUser().isPremium()) {
                subscriptionService.checkSubscription(s.getUser());
            }
        }
    }
}
