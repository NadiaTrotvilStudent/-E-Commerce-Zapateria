package com.zapateria.ecommerce.repository;

import com.zapateria.ecommerce.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Acceso a datos para carritos.
 */
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Busca el carrito activo de un usuario.
    Optional<Carrito> findByUsuarioId(Long usuarioId);
}
