package com.zapateria.ecommerce.repository;

import com.zapateria.ecommerce.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos para items del carrito.
 */
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByCarritoId(Long carritoId);
    Optional<ItemCarrito> findByCarritoIdAndVarianteProductoId(Long carritoId, Long varianteProductoId);
}
