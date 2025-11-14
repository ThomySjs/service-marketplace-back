package com.servicemarketplace.api.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.ServiceRejectCause;

@Repository
public interface ServiceRejectCauseRepository extends JpaRepository<ServiceRejectCause, Long> {

    @Query("""
        SELECT cause
        FROM ServiceRejectCause cause
    """)
    Page<ServiceRejectCause> findAllPaginated(Pageable pageable);
}

