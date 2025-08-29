package com.servicemarketplace.api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByName(String name);

    public Optional<User> findByEmail(String name);
}
