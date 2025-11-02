package com.servicemarketplace.api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.ServiceRejectCause;

@Repository
public interface ServiceRejectCauseRepository extends JpaRepository<ServiceRejectCause, Long> {

}

