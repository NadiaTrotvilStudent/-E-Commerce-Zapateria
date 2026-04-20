package com.zapateria.ecommerce.exception;

/**
 * Excepcion usada cuando se busca una entidad que no existe en base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
