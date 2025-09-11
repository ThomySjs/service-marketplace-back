package com.servicemarketplace.api.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.service.ServiceListResponse;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price)
              FROM Service s
              WHERE s.deleted = false
              AND s.seller = :seller
              """)
       List<ServiceListResponse> findBySeller(@Param("seller") User seller);

       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price)
              FROM Service s
              WHERE s.deleted = false
              AND s.category = :category
              """)
       List<ServiceListResponse> findByCategory(@Param("category") Category category);

       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price)
              FROM Service s
              WHERE s.deleted = false
              """)
       List<ServiceListResponse> findAllNotDeleted();

       Optional<Service> findByIdAndDeletedFalse(Long id);
}
