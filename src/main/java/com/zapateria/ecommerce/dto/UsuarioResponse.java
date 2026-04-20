package com.zapateria.ecommerce.dto;

/**
 * DTO de salida para mostrar usuarios sin exponer la password.
 */
public record UsuarioResponse(
        Long id,
        String username,
        String email,
        String nombre,
        String apellido
) {
}
