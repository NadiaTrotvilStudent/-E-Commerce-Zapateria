package com.zapateria.ecommerce.exception;

/**
 * Excepcion usada cuando el cliente envia datos invalidos
 * o intenta realizar una operacion no permitida por la logica de negocio.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
