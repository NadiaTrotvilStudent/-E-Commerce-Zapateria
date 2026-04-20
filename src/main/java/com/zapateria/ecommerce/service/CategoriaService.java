package com.zapateria.ecommerce.service;

import com.zapateria.ecommerce.dto.CategoriaRequest;
import com.zapateria.ecommerce.dto.CategoriaResponse;
import com.zapateria.ecommerce.exception.BadRequestException;
import com.zapateria.ecommerce.exception.ResourceNotFoundException;
import com.zapateria.ecommerce.model.Categoria;
import com.zapateria.ecommerce.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Logica de negocio asociada a categorias.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Lista todas las categorias como DTOs simples.
     */
    public List<CategoriaResponse> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> new CategoriaResponse(categoria.getId(), categoria.getNombre()))
                .toList();
    }

    /**
     * Crea una categoria validando que no exista otra con el mismo nombre.
     */
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        String nombre = request.nombre().trim();

        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BadRequestException("Ya existe una categoria con ese nombre");
        }

        Categoria categoria = Categoria.builder()
                .nombre(nombre)
                .build();

        Categoria guardada = categoriaRepository.save(categoria);
        return new CategoriaResponse(guardada.getId(), guardada.getNombre());
    }

    /**
     * Busca una categoria por id y devuelve la entidad completa.
     * Se usa cuando otra entidad necesita asociarse a una categoria.
     */
    public Categoria obtenerEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + id));
    }
}
