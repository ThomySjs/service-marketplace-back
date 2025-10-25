package com.servicemarketplace.api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servicemarketplace.api.domain.entities.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long>{

}
