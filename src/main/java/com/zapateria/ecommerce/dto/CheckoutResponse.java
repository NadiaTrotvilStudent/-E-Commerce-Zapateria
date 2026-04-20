package com.zapateria.ecommerce.dto;

import java.math.BigDecimal;

/**
 * DTO de salida luego de confirmar una compra.
 */
public record CheckoutResponse(
        String mensaje,
        Long ordenId,
        Long carritoId,
        Integer cantidadItemsProcesados,
        BigDecimal total
) {
}
