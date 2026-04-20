package com.zapateria.ecommerce.controller;

import com.zapateria.ecommerce.dto.CategoriaRequest;
import com.zapateria.ecommerce.dto.CategoriaResponse;
import com.zapateria.ecommerce.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller para consultar y crear categorias.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Lista todas las categorias disponibles.
     */
    @GetMapping
    public List<CategoriaResponse> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    /**
     * Crea una nueva categoria.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse crearCategoria(@Valid @RequestBody CategoriaRequest request) {
        return categoriaService.crearCategoria(request);
    }
}
