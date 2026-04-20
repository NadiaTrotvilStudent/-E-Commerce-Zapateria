package com.zapateria.ecommerce.dto;

import java.math.BigDecimal;

/**
 * DTO que representa un item del carrito ya armado para mostrar al cliente.
 */
public record ItemCarritoResponse(
        Long id,
        Long varianteProductoId,
        Long productoId,
        String productoNombre,
        String talle,
        String color,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
