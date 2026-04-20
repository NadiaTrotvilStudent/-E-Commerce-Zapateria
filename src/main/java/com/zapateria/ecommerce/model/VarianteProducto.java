package com.zapateria.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Variante concreta vendible de un producto.
 * Define combinacion de talle, color y stock.
 */
@Entity
@Table(name = "variantes_producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Talle de la variante.
    @Column(nullable = false, length = 20)
    private String talle;

    // Color de la variante.
    @Column(nullable = false, length = 40)
    private String color;

    // Stock real disponible de esta variante.
    @Column(nullable = false)
    private Integer stock;

    // Producto general al que pertenece esta variante.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonBackReference
    private Producto producto;
}
