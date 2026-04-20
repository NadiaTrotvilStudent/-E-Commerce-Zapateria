package com.zapateria.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para registrar un usuario nuevo.
 */
public record UsuarioRegistroRequest(
        @NotBlank(message = "El username es obligatorio")
        @Size(max = 50, message = "El username no puede superar los 50 caracteres")
        String username,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es valido")
        @Size(max = 100, message = "El email no puede superar los 100 caracteres")
        String email,

        @NotBlank(message = "La password es obligatoria")
        @Size(min = 4, max = 100, message = "La password debe tener entre 4 y 100 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
        String apellido
) {
}
