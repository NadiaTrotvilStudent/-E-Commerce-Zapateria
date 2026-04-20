package com.zapateria.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para el login simple.
 */
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "La password es obligatoria")
        String password
) {
}
