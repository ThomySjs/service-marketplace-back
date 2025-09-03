package com.servicemarketplace.api.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
}
