package com.zapateria.ecommerce.controller;

import com.zapateria.ecommerce.dto.ProductoRequest;
import com.zapateria.ecommerce.dto.ProductoResponse;
import com.zapateria.ecommerce.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para la gestion de productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Lista todos los productos o los filtra por categoria si se envia categoriaId.
     */
    @GetMapping
    public List<ProductoResponse> listarProductos(@RequestParam(required = false) Long categoriaId) {
        return productoService.listarProductos(categoriaId);
    }

    /**
     * Devuelve el detalle completo de un producto.
     */
    @GetMapping("/{id}")
    public ProductoResponse obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProducto(id);
    }

    /**
     * Crea un producto nuevo.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crearProducto(@Valid @RequestBody ProductoRequest producto) {
        return productoService.guardarProducto(producto);
    }

    /**
     * Actualiza los datos generales de un producto.
     */
    @PutMapping("/{id}")
    public ProductoResponse actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest producto) {
        return productoService.actualizarProducto(id, producto);
    }

    /**
     * Elimina un producto por su id.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
}
