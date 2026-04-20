package com.zapateria.ecommerce.controller;

import com.zapateria.ecommerce.dto.LoginRequest;
import com.zapateria.ecommerce.dto.LoginResponse;
import com.zapateria.ecommerce.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller encargado del login simple de usuarios.
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Recibe email y password y valida si corresponden a un usuario existente.
     */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }
}
