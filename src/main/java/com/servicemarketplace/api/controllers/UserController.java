package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.UserDTO;
import com.servicemarketplace.api.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Permite a un usuario ver los detalles de su cuenta.")
    /**
     * El usuario tiene que estar logueado para obtener los detalles de su cuenta
     */
    @GetMapping("/details")
    public ResponseEntity<UserDTO> getAccountDetails() {
        return ResponseEntity.ok(userService.getAccountDetails());
    }

}
