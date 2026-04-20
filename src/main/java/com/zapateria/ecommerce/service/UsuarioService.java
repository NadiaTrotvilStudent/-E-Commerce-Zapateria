package com.zapateria.ecommerce.service;

import com.zapateria.ecommerce.dto.LoginRequest;
import com.zapateria.ecommerce.dto.LoginResponse;
import com.zapateria.ecommerce.dto.UsuarioRegistroRequest;
import com.zapateria.ecommerce.dto.UsuarioResponse;
import com.zapateria.ecommerce.exception.BadRequestException;
import com.zapateria.ecommerce.model.Usuario;
import com.zapateria.ecommerce.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Logica de negocio asociada a usuarios y login simple.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Lista todos los usuarios registrados.
     */
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Registra un usuario nuevo validando email y username unicos.
     */
    public UsuarioResponse registrarUsuario(UsuarioRegistroRequest request) {
        String email = request.email().trim().toLowerCase();
        String username = request.username().trim();

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Ya existe un usuario con ese email");
        }

        if (usuarioRepository.existsByUsernameIgnoreCase(username)) {
            throw new BadRequestException("Ya existe un usuario con ese username");
        }

        Usuario usuario = Usuario.builder()
                .username(username)
                .email(email)
                .password(request.password())
                .nombre(request.nombre().trim())
                .apellido(request.apellido().trim())
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    /**
     * Login simple: busca por email y compara la password enviada.
     */
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new BadRequestException("Credenciales invalidas"));

        if (!usuario.getPassword().equals(request.password())) {
            throw new BadRequestException("Credenciales invalidas");
        }

        return new LoginResponse("Login correcto", toResponse(usuario));
    }

    /**
     * Convierte una entidad Usuario a DTO seguro.
     */
    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellido()
        );
    }
}
