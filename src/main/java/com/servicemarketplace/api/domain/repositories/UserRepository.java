package com.servicemarketplace.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByName(String name);

    @Query ("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.services s
        WHERE u.email = :email
    """)
    public Optional<User> getUserDetailsByEmail(@Param("email") String email);

    public Optional<User> findByEmail(String name);
}
