package com.zapateria.ecommerce.service;

import com.zapateria.ecommerce.dto.ActualizarCantidadCarritoRequest;
import com.zapateria.ecommerce.dto.AgregarItemCarritoRequest;
import com.zapateria.ecommerce.dto.CarritoResponse;
import com.zapateria.ecommerce.dto.CheckoutResponse;
import com.zapateria.ecommerce.dto.ItemCarritoResponse;
import com.zapateria.ecommerce.exception.BadRequestException;
import com.zapateria.ecommerce.exception.ResourceNotFoundException;
import com.zapateria.ecommerce.model.Carrito;
import com.zapateria.ecommerce.model.DetalleOrden;
import com.zapateria.ecommerce.model.ItemCarrito;
import com.zapateria.ecommerce.model.Orden;
import com.zapateria.ecommerce.model.Producto;
import com.zapateria.ecommerce.model.Usuario;
import com.zapateria.ecommerce.model.VarianteProducto;
import com.zapateria.ecommerce.repository.CarritoRepository;
import com.zapateria.ecommerce.repository.ItemCarritoRepository;
import com.zapateria.ecommerce.repository.OrdenRepository;
import com.zapateria.ecommerce.repository.ProductoRepository;
import com.zapateria.ecommerce.repository.UsuarioRepository;
import com.zapateria.ecommerce.repository.VarianteProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio con la logica principal del carrito y del checkout.
 */
@Service
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final OrdenRepository ordenRepository;
    private final VarianteProductoRepository varianteProductoRepository;

    public CarritoService(
            CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            OrdenRepository ordenRepository,
            VarianteProductoRepository varianteProductoRepository
    ) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.ordenRepository = ordenRepository;
        this.varianteProductoRepository = varianteProductoRepository;
    }

    /**
     * Obtiene el carrito de un usuario o lo crea si todavia no existe.
     */
    public CarritoResponse obtenerCarritoPorUsuario(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        return toResponse(carrito);
    }

    /**
     * Agrega una variante al carrito.
     * Si ya existia, suma cantidades en vez de crear otro item duplicado.
     */
    public CarritoResponse agregarProducto(Long usuarioId, AgregarItemCarritoRequest request) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        VarianteProducto varianteProducto = buscarVariante(request.varianteProductoId());
        int nuevaCantidad;

        ItemCarrito item = itemCarritoRepository.findByCarritoIdAndVarianteProductoId(
                        carrito.getId(),
                        varianteProducto.getId()
                )
                .orElse(null);

        if (item == null) {
            nuevaCantidad = request.cantidad();
            validarStock(varianteProducto, nuevaCantidad);

            item = ItemCarrito.builder()
                    .carrito(carrito)
                    .varianteProducto(varianteProducto)
                    .cantidad(request.cantidad())
                    .precioUnitario(varianteProducto.getProducto().getPrecio())
                    .build();
            carrito.getItems().add(item);
        } else {
            nuevaCantidad = item.getCantidad() + request.cantidad();
            validarStock(varianteProducto, nuevaCantidad);
            item.setCantidad(nuevaCantidad);
            item.setPrecioUnitario(varianteProducto.getProducto().getPrecio());
        }

        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    /**
     * Cambia la cantidad de un item del carrito validando stock.
     */
    public CarritoResponse actualizarCantidad(Long usuarioId, Long itemId, ActualizarCantidadCarritoRequest request) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        ItemCarrito item = buscarItemEnCarrito(carrito, itemId);

        validarStock(item.getVarianteProducto(), request.cantidad());
        item.setCantidad(request.cantidad());
        item.setPrecioUnitario(item.getVarianteProducto().getProducto().getPrecio());

        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    /**
     * Elimina un item puntual del carrito.
     */
    public CarritoResponse eliminarItem(Long usuarioId, Long itemId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        ItemCarrito item = buscarItemEnCarrito(carrito, itemId);

        carrito.getItems().remove(item);
        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    /**
     * Vacia por completo el carrito del usuario.
     */
    public CarritoResponse vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        carrito.getItems().clear();
        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    /**
     * Confirma la compra.
     * Valida stock por variante, descuenta unidades, genera una orden
     * y recalcula el stock total de cada producto involucrado.
     */
    public CheckoutResponse checkout(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        Usuario usuario = carrito.getUsuario();

        if (carrito.getItems().isEmpty()) {
            throw new BadRequestException("El carrito esta vacio");
        }

        for (ItemCarrito item : carrito.getItems()) {
            validarStock(item.getVarianteProducto(), item.getCantidad());
        }

        int cantidadItemsProcesados = 0;
        BigDecimal total = BigDecimal.ZERO;
        Orden orden = Orden.builder()
                .usuario(usuario)
                .build();

        for (ItemCarrito item : carrito.getItems()) {
            VarianteProducto varianteProducto = item.getVarianteProducto();
            Producto producto = varianteProducto.getProducto();
            BigDecimal subtotal = item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()));

            varianteProducto.setStock(varianteProducto.getStock() - item.getCantidad());
            cantidadItemsProcesados += item.getCantidad();
            total = total.add(subtotal);

            DetalleOrden detalle = DetalleOrden.builder()
                    .orden(orden)
                    .varianteProducto(varianteProducto)
                    .productoNombre(producto.getNombre())
                    .talle(varianteProducto.getTalle())
                    .color(varianteProducto.getColor())
                    .cantidad(item.getCantidad())
                    .precioUnitario(item.getPrecioUnitario())
                    .subtotal(subtotal)
                    .build();
            orden.getDetalles().add(detalle);
        }

        orden.setTotal(total);
        orden = ordenRepository.save(orden);

        varianteProductoRepository.saveAll(
                carrito.getItems().stream().map(ItemCarrito::getVarianteProducto).toList()
        );

        carrito.getItems().stream()
                .map(item -> item.getVarianteProducto().getProducto())
                .distinct()
                .forEach(this::recalcularStockProducto);

        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return new CheckoutResponse(
                "Checkout realizado correctamente",
                orden.getId(),
                carrito.getId(),
                cantidadItemsProcesados,
                total
        );
    }

    /**
     * Busca el carrito del usuario o lo crea si todavia no tenia uno.
     */
    private Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> crearCarrito(buscarUsuario(usuarioId)));
    }

    /**
     * Crea un carrito vacio para un usuario.
     */
    private Carrito crearCarrito(Usuario usuario) {
        Carrito carrito = Carrito.builder()
                .usuario(usuario)
                .build();
        return carritoRepository.save(carrito);
    }

    /**
     * Busca el usuario por id.
     */
    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId));
    }

    /**
     * Busca el producto general por id.
     * Se mantiene por si la logica futura necesita acceder directamente al producto.
     */
    private Producto buscarProducto(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + productoId));
    }

    /**
     * Busca una variante concreta por id.
     */
    private VarianteProducto buscarVariante(Long varianteProductoId) {
        return varianteProductoRepository.findById(varianteProductoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variante de producto no encontrada con id " + varianteProductoId
                ));
    }

    /**
     * Busca un item dentro del carrito actual.
     */
    private ItemCarrito buscarItemEnCarrito(Carrito carrito, Long itemId) {
        return carrito.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado con id " + itemId));
    }

    /**
     * Valida que la variante tenga stock suficiente para la cantidad pedida.
     */
    private void validarStock(VarianteProducto varianteProducto, int cantidadSolicitada) {
        if (varianteProducto.getStock() < cantidadSolicitada) {
            throw new BadRequestException(
                    "Stock insuficiente para la variante seleccionada del producto "
                            + varianteProducto.getProducto().getNombre()
            );
        }
    }

    /**
     * Recalcula el stock total del producto a partir de sus variantes.
     */
    private void recalcularStockProducto(Producto producto) {
        int stockTotal = varianteProductoRepository.findByProductoIdOrderByColorAscTalleAsc(producto.getId()).stream()
                .mapToInt(VarianteProducto::getStock)
                .sum();
        producto.setStock(stockTotal);
        productoRepository.save(producto);
    }

    /**
     * Convierte el carrito en un DTO listo para mostrar.
     */
    private CarritoResponse toResponse(Carrito carrito) {
        List<ItemCarritoResponse> items = carrito.getItems().stream()
                .map(item -> new ItemCarritoResponse(
                        item.getId(),
                        item.getVarianteProducto().getId(),
                        item.getVarianteProducto().getProducto().getId(),
                        item.getVarianteProducto().getProducto().getNombre(),
                        item.getVarianteProducto().getTalle(),
                        item.getVarianteProducto().getColor(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad()))
                ))
                .toList();

        int cantidadItems = items.stream()
                .mapToInt(ItemCarritoResponse::cantidad)
                .sum();

        BigDecimal total = items.stream()
                .map(ItemCarritoResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarritoResponse(
                carrito.getId(),
                carrito.getUsuario().getId(),
                carrito.getUsuario().getUsername(),
                items,
                cantidadItems,
                total,
                carrito.getFechaCreacion(),
                carrito.getFechaActualizacion()
        );
    }
}
