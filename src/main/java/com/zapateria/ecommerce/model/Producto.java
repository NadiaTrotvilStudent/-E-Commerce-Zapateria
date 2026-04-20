package com.zapateria.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo general del articulo publicado en el catalogo.
 * No representa una unidad vendible puntual, sino el producto base.
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Stock total del producto, calculado como suma de sus variantes.
    @Column(nullable = false)
    private Integer stock;

    @Column(length = 255)
    private String imagenUrl;

    // Marca del producto.
    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    // Tipo comercial del articulo: zapatilla, bota, cinto, etc.
    @ManyToOne
    @JoinColumn(name = "tipo_producto_id", nullable = false)
    private TipoProducto tipoProducto;

    // Genero al que se orienta el producto.
    @ManyToOne
    @JoinColumn(name = "genero_id", nullable = false)
    private Genero genero;

    // Categoria del catalogo.
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Usuario que publico el producto.
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioCreador;

    // Variantes reales vendibles del producto.
    @Builder.Default
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<VarianteProducto> variantes = new ArrayList<>();
}
