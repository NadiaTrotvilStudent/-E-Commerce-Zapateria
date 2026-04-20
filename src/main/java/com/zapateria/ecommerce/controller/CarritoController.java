package com.zapateria.ecommerce.controller;

import com.zapateria.ecommerce.dto.ActualizarCantidadCarritoRequest;
import com.zapateria.ecommerce.dto.AgregarItemCarritoRequest;
import com.zapateria.ecommerce.dto.CarritoResponse;
import com.zapateria.ecommerce.dto.CheckoutResponse;
import com.zapateria.ecommerce.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone las operaciones principales del carrito de compras.
 */
@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    /**
     * Devuelve el carrito actual de un usuario.
     */
    @GetMapping
    public CarritoResponse obtenerCarrito(@RequestParam Long usuarioId) {
        return carritoService.obtenerCarritoPorUsuario(usuarioId);
    }

    /**
     * Agrega una variante de producto al carrito.
     */
    @PostMapping
    public CarritoResponse agregarProducto(
            @RequestParam Long usuarioId,
            @Valid @RequestBody AgregarItemCarritoRequest request
    ) {
        return carritoService.agregarProducto(usuarioId, request);
    }

    /**
     * Cambia la cantidad de un item ya existente dentro del carrito.
     */
    @PatchMapping("/items/{itemId}")
    public CarritoResponse actualizarCantidad(
            @RequestParam Long usuarioId,
            @PathVariable Long itemId,
            @Valid @RequestBody ActualizarCantidadCarritoRequest request
    ) {
        return carritoService.actualizarCantidad(usuarioId, itemId, request);
    }

    /**
     * Elimina un item especifico del carrito.
     */
    @DeleteMapping("/items/{itemId}")
    public CarritoResponse eliminarItem(@RequestParam Long usuarioId, @PathVariable Long itemId) {
        return carritoService.eliminarItem(usuarioId, itemId);
    }

    /**
     * Borra todos los items del carrito del usuario.
     */
    @DeleteMapping
    public CarritoResponse vaciarCarrito(@RequestParam Long usuarioId) {
        return carritoService.vaciarCarrito(usuarioId);
    }

    /**
     * Finaliza la compra, valida stock y genera la orden.
     */
    @PostMapping("/checkout")
    public CheckoutResponse checkout(@RequestParam Long usuarioId) {
        return carritoService.checkout(usuarioId);
    }
}
