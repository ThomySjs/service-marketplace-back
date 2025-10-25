package com.servicemarketplace.api.services;

import java.util.List;

import com.servicemarketplace.api.domain.entities.Membership;
import com.servicemarketplace.api.dto.membership.MembershipDTO;

public interface MembershipService {

    public Membership create(MembershipDTO dto);

    public Membership update(Membership dto);

    public void delete(Long id);

    public List<Membership> getAll();
}
