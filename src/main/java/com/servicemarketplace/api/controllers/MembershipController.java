package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.domain.entities.Membership;
import com.servicemarketplace.api.dto.membership.MembershipDTO;
import com.servicemarketplace.api.services.MembershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping()
    public ResponseEntity<List<Membership>> getAll() {
        return ResponseEntity.ok(membershipService.getAll());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Membership> create(@Valid @RequestBody MembershipDTO dto) {
        return ResponseEntity.ok(membershipService.create(dto));
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Membership> update(@Valid @RequestBody Membership updatMembership) {
        return ResponseEntity.ok(membershipService.update(updatMembership));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        membershipService.delete(id);
        return ResponseEntity.ok().build();
    }
}
