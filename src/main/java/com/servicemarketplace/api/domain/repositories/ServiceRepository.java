package com.servicemarketplace.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.Service;
import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.service.ServiceDetailsResponse;
import com.servicemarketplace.api.dto.service.ServiceListResponse;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price,
              s.description)
              FROM Service s
              WHERE s.deleted = false
              AND s.seller = :seller
              """)
       Page<ServiceListResponse> findBySeller(@Param("seller") User seller, Pageable pageable);

       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price,
              s.description)
              FROM Service s
              WHERE s.deleted = false
              AND s.category.id IN :category
              """)
       Page<ServiceListResponse> findByCategory(@Param("category") String[] category, Pageable pageable);

       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price,
              s.description)
              FROM Service s
              WHERE s.deleted = false
              AND s.title LIKE %:title%
              """)
       Page<ServiceListResponse> findByTitle(@Param("title") String title, Pageable pageable);

       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price,
              s.description)
              FROM Service s
              WHERE s.deleted = false
              AND s.category.id IN :category
              AND s.title LIKE %:title%
              """)
       Page<ServiceListResponse> findByCategoryAndTitleNotDeleted(@Param("category") String[] category, @Param("title") String title, Pageable pageable);

       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceListResponse( +
              s.id,
              s.category.title,
              s.imagePath,
              s.title,
              s.price,
              s.description)
              FROM Service s
              WHERE s.deleted = false
              """)
       Page<ServiceListResponse> findAllNotDeleted(Pageable pageable);

       @Query("""
              SELECT new com.servicemarketplace.api.dto.service.ServiceDetailsResponse(
              s.id,
              s.title,
              s.description,
              s.price,
              s.imagePath,
              new com.servicemarketplace.api.dto.service.SellerDTO(s.seller.id, s.seller.name, s.seller.phone, s.seller.email),
              s.category.title)
              FROM Service s
              WHERE s.deleted = false
              AND s.id = :id
              """)
       Optional<ServiceDetailsResponse> getServiceDetailsNotDeleted(@Param("id") Long id);

       Optional<Service> findByIdAndDeletedFalse(Long id);
}
