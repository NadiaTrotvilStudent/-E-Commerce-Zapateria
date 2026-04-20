package com.zapateria.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Categoria general del producto.
 * Sirve para agrupar articulos del catalogo.
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}
