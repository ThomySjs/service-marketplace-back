package com.servicemarketplace.api.services;

import java.util.List;
import java.util.Map;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.servicemarketplace.api.domain.entities.Subscription;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.transaction.SubscriptionDTO;
import com.servicemarketplace.api.dto.transaction.SubscriptionResponseDTO;

public interface SubscriptionService {

    public SubscriptionResponseDTO create(SubscriptionDTO dto);

    public void delete(Long id);

    public List<SubscriptionResponseDTO> getAll();

    public Subscription getById(Long id);

    public String checkout(Long membershipId, String header);

    public void handlePreapproval(Map<String, String> data) throws MPApiException, MPException;

    public void handlePayment(Map<String, String> data) throws MPApiException, MPException;

    public User checkSubscription(User user);
}
