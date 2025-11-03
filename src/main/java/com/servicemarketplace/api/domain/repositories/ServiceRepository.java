package com.servicemarketplace.api.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;
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
              AND s.status = 'PENDING'
              ORDER BY s.createdDate ASC
              """)
		 Page<ServiceListResponse> findByStatusPending(Pageable pageable);

		@Query("""
			  SELECT 
					YEAR(s.createdDate) AS year,
					MONTH(s.createdDate) AS month,
					s.status AS status,
					COUNT(s) AS count
			  FROM Service s
			  WHERE s.createdDate >= :fromDate
			  GROUP BY YEAR(s.createdDate), MONTH(s.createdDate), s.status
			  ORDER BY YEAR(s.createdDate), MONTH(s.createdDate)
		 """)
		List<Object[]> findServiceStatsFromDate(LocalDateTime fromDate);

		@Query("""
		 SELECT s.status, COUNT(s)
		 FROM Service s
		 WHERE s.createdDate >= :fromDate
		 GROUP BY s.status
		""")
		List<Object[]> findServiceCountByStatusFromDate(LocalDateTime fromDate);

		@Query("""
		 SELECT c.id, c.message, COUNT(s)
		 FROM Service s
		 JOIN s.serviceRejectCause c
		 WHERE s.createdDate >= :fromDate
		 AND s.status = 'REJECTED'
		 GROUP BY c.id, c.message
		 ORDER BY COUNT(s) DESC
		""")
		List<Object[]> findRejectedCountByCauseFromDate(LocalDateTime fromDate);

}
