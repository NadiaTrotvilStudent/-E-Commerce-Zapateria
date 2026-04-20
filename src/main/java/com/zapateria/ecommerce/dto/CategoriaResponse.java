package com.zapateria.ecommerce.dto;

/**
 * DTO simple para devolver categorias.
 */
public record CategoriaResponse(
        Long id,
        String nombre
) {
}
