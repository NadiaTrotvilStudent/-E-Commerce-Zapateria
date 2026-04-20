package com.zapateria.ecommerce.service;

import com.zapateria.ecommerce.dto.VarianteProductoRequest;
import com.zapateria.ecommerce.dto.VarianteProductoResponse;
import com.zapateria.ecommerce.exception.BadRequestException;
import com.zapateria.ecommerce.exception.ResourceNotFoundException;
import com.zapateria.ecommerce.model.Producto;
import com.zapateria.ecommerce.model.VarianteProducto;
import com.zapateria.ecommerce.repository.ProductoRepository;
import com.zapateria.ecommerce.repository.VarianteProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para consultar y crear variantes de producto.
 */
@Service
public class VarianteProductoService {

    private final VarianteProductoRepository varianteProductoRepository;
    private final ProductoRepository productoRepository;

    public VarianteProductoService(
            VarianteProductoRepository varianteProductoRepository,
            ProductoRepository productoRepository
    ) {
        this.varianteProductoRepository = varianteProductoRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Lista las variantes de un producto.
     */
    public List<VarianteProductoResponse> listarPorProducto(Long productoId) {
        validarProductoExiste(productoId);
        return varianteProductoRepository.findByProductoIdOrderByColorAscTalleAsc(productoId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Crea una variante nueva y luego recalcula el stock total del producto.
     */
    public VarianteProductoResponse crearVariante(Long productoId, VarianteProductoRequest request) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + productoId));

        if (request.stock() < 0) {
            throw new BadRequestException("El stock de la variante no puede ser negativo");
        }

        VarianteProducto variante = VarianteProducto.builder()
                .talle(request.talle().trim())
                .color(request.color().trim())
                .stock(request.stock())
                .producto(producto)
                .build();

        VarianteProducto guardada = varianteProductoRepository.save(variante);
        recalcularStockProducto(producto);
        return toResponse(guardada);
    }

    /**
     * Recalcula el stock total del producto como suma de sus variantes.
     */
    private void recalcularStockProducto(Producto producto) {
        int stockTotal = varianteProductoRepository.findByProductoIdOrderByColorAscTalleAsc(producto.getId()).stream()
                .mapToInt(VarianteProducto::getStock)
                .sum();
        producto.setStock(stockTotal);
        productoRepository.save(producto);
    }

    /**
     * Verifica que el producto exista antes de listar sus variantes.
     */
    private void validarProductoExiste(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new ResourceNotFoundException("Producto no encontrado con id " + productoId);
        }
    }

    /**
     * Convierte la entidad VarianteProducto a su DTO de salida.
     */
    private VarianteProductoResponse toResponse(VarianteProducto variante) {
        return new VarianteProductoResponse(
                variante.getId(),
                variante.getTalle(),
                variante.getColor(),
                variante.getStock()
        );
    }
}
