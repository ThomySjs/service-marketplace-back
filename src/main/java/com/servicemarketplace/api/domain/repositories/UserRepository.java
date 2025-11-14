package com.servicemarketplace.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.User;
import com.servicemarketplace.api.dto.user.UserForAdmin;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByName(String name);

    @Query ("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.services s
        WHERE u.email = :email
    """)
    public Optional<User> getUserDetailsByEmail(@Param("email") String email);

    @Query ("""
        SELECT new com.servicemarketplace.api.dto.user.UserForAdmin(
            u.id,
            u.name,
            u.email,
            u.address,
            u.phone,
            u.role,
            u.verified,
            u.createdAt
        )
        FROM User u
        WHERE u.deleted = false
    """)
    Page<UserForAdmin> getUserForAdmin(Pageable pageable);

    public Optional<User> findByEmail(String name);
}
