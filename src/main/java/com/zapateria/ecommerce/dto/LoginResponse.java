package com.zapateria.ecommerce.dto;

/**
 * DTO de salida cuando un usuario hace login correctamente.
 */
public record LoginResponse(
        String mensaje,
        UsuarioResponse usuario
) {
}
