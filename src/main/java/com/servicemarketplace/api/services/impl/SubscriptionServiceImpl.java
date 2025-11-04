package com.servicemarketplace.api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preapproval.PreApprovalAutoRecurringCreateRequest;
import com.mercadopago.client.preapproval.PreapprovalClient;
import com.mercadopago.client.preapproval.PreapprovalCreateRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preapproval.Preapproval;
import com.servicemarketplace.api.config.JwtUtils;
import com.servicemarketplace.api.config.Roles;
import com.servicemarketplace.api.config.SubscriptionState;
import com.servicemarketplace.api.config.CustomConfig.UrlConfig;
import com.servicemarketplace.api.domain.entities.Membership;
import com.servicemarketplace.api.domain.entities.Subscription;
import com.servicemarketplace.api.domain.entities.Transaction;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.domain.repositories.MembershipRepository;
import com.servicemarketplace.api.domain.repositories.SubscriptionRepository;
import com.servicemarketplace.api.domain.repositories.TransactionRepository;
import com.servicemarketplace.api.domain.repositories.UserRepository;
import com.servicemarketplace.api.dto.transaction.SubscriptionDTO;
import com.servicemarketplace.api.dto.transaction.SubscriptionResponseDTO;
import com.servicemarketplace.api.exceptions.auth.ResourceNotFoundException;
import com.servicemarketplace.api.services.SubscriptionService;
import com.servicemarketplace.api.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ServiceServiceImpl serviceServiceImpl;

    private final JwtUtils jwtUtils;
    private final UrlConfig urlConfig;
    private final SubscriptionRepository subscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MembershipRepository membershipRepository;

    @Override
    public SubscriptionResponseDTO create(SubscriptionDTO dto) {
        User user = userService.getUserByEmail(dto.email());
        Optional<Membership> membership = membershipRepository.findById(dto.membershipId());

        if (user == null || membership.isEmpty()) {
            throw new ResourceNotFoundException("Usuario o membresía no encontrado.");
        }

        Subscription subscription = subscriptionRepository.getByUserEmail(user.getEmail()).orElseGet(() -> {
            Subscription newSubscription = new Subscription();
            newSubscription.setUser(user);
            return newSubscription;
        });

        subscription.setMembership(membership.get());
        //Esto previene que al cambiar de membresia del usuario se mantenga el estado activo
        subscription.setState(SubscriptionState.INACTIVE.name());

        subscriptionRepository.save(subscription);

        SubscriptionResponseDTO subscriptionResponseDTO = new SubscriptionResponseDTO();
        subscriptionResponseDTO.fromSubscription(subscription);

        return subscriptionResponseDTO;
    }

    @Override
    public void delete(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public List<SubscriptionResponseDTO> getAll() {
        return subscriptionRepository.getAll();
    }

    @Override
    public Subscription getById(Long id) {
        Optional<Subscription> subscription = subscriptionRepository.findById(id);

        if (subscription.isEmpty()) {
            throw new ResourceNotFoundException("No se encontro la subscripción.");
        }

        return subscription.get();
    }

    @Override
    public String checkout(Long membershipId, String header) {
        //Obtiene el usuario del header
        String token = jwtUtils.parseJwtFromString(header);
        String email = jwtUtils.getUserFromToken(token);
        User user = userService.getUserByEmail(email);

        Optional<Membership> membership = membershipRepository.findById(membershipId);

        if (user == null || membership.isEmpty()) {
            throw new ResourceNotFoundException("Usuario o membresía no encontrado.");
        }
        Membership foundMembership = membership.get();

        //Crea una subscripcion si el usuario no tenia ninguna registrada, sino usa la existente
        Subscription subscription = subscriptionRepository.getByUserId(user.getId())
            .orElseGet(() -> {
                Subscription newSubscription = new Subscription();
                newSubscription.setUser(user);
                return newSubscription;
            }
        );

        subscription.setMembership(foundMembership);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        OffsetDateTime startDate = OffsetDateTime.now();
        OffsetDateTime endDate = startDate.plusYears(1);

        PreApprovalAutoRecurringCreateRequest preapprovalAutoRecurring =
            PreApprovalAutoRecurringCreateRequest.builder()
                .currencyId("ARS")
                .frequency(1)
                .frequencyType("months")
                .transactionAmount(BigDecimal.valueOf(foundMembership.getPrice()))
                .startDate(startDate)
                .endDate(endDate)
                .build();

        PreapprovalCreateRequest preapproval =
            PreapprovalCreateRequest.builder()
                .autoRecurring(preapprovalAutoRecurring)
                .payerEmail(user.getEmail())
                .backUrl(urlConfig.getFrontendUrl() + "services")
                .reason(foundMembership.getName())
                .externalReference(savedSubscription.getId().toString())
                .build();

        PreapprovalClient client = new PreapprovalClient();

        try {
            Preapproval response = client.create(preapproval);

            //SandboxInitPoint es para desarrollo, para prod usar InitPoint
            return response.getInitPoint();
        }catch (MPApiException e) {
            System.out.println("Error de API: " + e.getApiResponse().getContent());
            return null;
        }catch (MPException e) {
            System.out.println("Error : " + e);
            return null;
        }
    }

    public void handlePreapproval(Map<String, String> data) throws MPApiException, MPException{
        String id = data.get("id");

        PreapprovalClient preapprovalClient = new PreapprovalClient();
        Preapproval subscriptionDetails = preapprovalClient.get(id);
        String externalReference = subscriptionDetails.getExternalReference();
        String status = subscriptionDetails.getStatus();

        Optional<Subscription> subscription = subscriptionRepository.findById(Long.valueOf(externalReference));

        if (subscription.isEmpty()) {
            throw new ResourceNotFoundException("Subscripción invalida");
        }

        Subscription foundSubscription = subscription.get();

        if (status.equals("authorized")) {
            foundSubscription.setState(SubscriptionState.ACTIVE.name());
        } else if (status.equals("cancelled")) {
            foundSubscription.setState(SubscriptionState.INACTIVE.name());
        }

        subscriptionRepository.save(foundSubscription);
    }

    public void handlePayment(Map<String, String> data) throws MPApiException, MPException {
        String id = data.get("id");
        PaymentClient paymentClient = new PaymentClient();
        Payment paymentDetails = paymentClient.get(Long.valueOf(id));

        Long payment_id = paymentDetails.getId();
        String externalReference = paymentDetails.getExternalReference();
        String status = paymentDetails.getStatus();
        Double total = Double.valueOf(paymentDetails.getTransactionAmount().toString());

        Optional<Subscription> subscription = subscriptionRepository.findById(Long.valueOf(externalReference));

        if (subscription.isEmpty()) {
            throw new ResourceNotFoundException("Subscripción invalida");
        }

        Subscription foundSubscription = subscription.get();
        User user = foundSubscription.getUser();

        if (status.equals("approved")) {
            user.setRole(Roles.PREMIUM.name());
            foundSubscription.setEndDate(LocalDateTime.now().plusMonths(1));
        } else if ((status.equals("rejected") || status.equals("cancelled")) && user.getRole().equals(Roles.PREMIUM.name())) {
            user.setRole(Roles.USER.name());
        }

        Transaction transaction = transactionRepository.getByMpId(payment_id)
            .orElseGet(() -> {
                Transaction newTransaction = new Transaction();
                newTransaction.setMpId(payment_id);
                newTransaction.setSubscription(foundSubscription);
                newTransaction.setTotal(total);

                return newTransaction;
            });

        transaction.setState(status);

        transactionRepository.save(transaction);
        subscriptionRepository.save(foundSubscription);
        userRepository.save(user);
    }

    public User checkSubscription(User user) {
        Subscription subscription = subscriptionRepository.getByUserId(user.getId()).get();

        if (subscription.getEndDate().isBefore(LocalDateTime.now())) {
            user.setRole(Roles.USER.name());
            user = serviceServiceImpl.disableExtraServices(user);
            return userRepository.save(user);
        }

        return user;
    }
}
