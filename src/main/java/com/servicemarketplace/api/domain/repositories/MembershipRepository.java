package com.servicemarketplace.api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long>{

}
