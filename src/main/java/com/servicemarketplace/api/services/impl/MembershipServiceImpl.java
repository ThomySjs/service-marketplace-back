package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.servicemarketplace.api.domain.entities.Membership;
import com.servicemarketplace.api.domain.repositories.MembershipRepository;
import com.servicemarketplace.api.dto.membership.MembershipDTO;
import com.servicemarketplace.api.exceptions.auth.ResourceNotFoundException;
import com.servicemarketplace.api.services.MembershipService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    public Membership create(MembershipDTO dto) {
        Membership membership = new Membership();
        membership.fromDto(dto);
        return membershipRepository.save(membership);
    }

    @Override
    public Membership update(Membership updatedMembership) {
        Optional<Membership> membership = membershipRepository.findById(updatedMembership.getId());

        if (membership.isEmpty()) {
            throw new ResourceNotFoundException("Membresía no encontrada.");
        }

        return membershipRepository.save(updatedMembership);
    }

    @Override
    public void delete(Long id) {
        membershipRepository.deleteById(id);
    }

    @Override
    public List<Membership> getAll() {
        return membershipRepository.findAll();
    }
}
