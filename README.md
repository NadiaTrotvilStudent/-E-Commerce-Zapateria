# E-Commerce Zapateria

Proyecto backend desarrollado en Spring Boot para un trabajo practico de una aplicacion web de e-commerce orientada inicialmente a una zapateria, con posibilidad de extender el catalogo a otros articulos como cintos y accesorios.

El sistema fue construido con arquitectura monolitica en 3 capas:
- `controller`
- `service`
- `repository`

Package principal del proyecto:

```text
com.zapateria.ecommerce
```

## Resumen Del Proyecto

La aplicacion permite:
- registrar usuarios
- realizar login simple
- consultar catalogo de productos
- administrar categorias, marcas, generos y tipos de producto
- modelar productos con variantes
- gestionar carrito de compras
- ejecutar checkout validando stock
- guardar historial de compras mediante ordenes
- exponer toda la funcionalidad mediante API REST

## Consigna Del Trabajo

La consigna del trabajo solicita desarrollar una aplicacion web de e-commerce que permita:
- gestion de usuarios
- autenticacion de usuarios
- catalogo de productos
- detalle de producto
- carrito de compras
- checkout sin pago real
- publicacion y gestion de productos
- persistencia de datos
- construccion de API REST
- uso de Spring Boot, Spring Data JPA, Lombok y Maven
- integracion con base de datos
- arquitectura en capas
- modelado explicito de entidades y relaciones JPA/Hibernate
- manejo controlado de excepciones
- incorporacion de Spring Security

## Que Se Hizo En Este Proyecto

Se implemento:
- estructura backend en 3 capas
- entidades JPA con relaciones explicitas
- persistencia con Spring Data JPA
- base H2 para desarrollo y pruebas
- endpoints REST para usuarios, login, categorias, marcas, generos, tipos de producto, productos, variantes, carrito y ordenes
- DTOs de entrada y salida
- validaciones con `@Valid`, `@NotBlank`, `@NotNull` y `@Email`
- manejo global de errores con `@RestControllerAdvice`
- carga inicial de datos de prueba con `DataLoader`
- checkout con validacion de stock
- descuento de stock en checkout
- historial de compra con `Orden` y `DetalleOrden`
- documentacion tecnica y funcional en carpeta `docs/`
- collection de Postman para pruebas manuales

Ademas, el modelo fue mejorado para un e-commerce de indumentaria/calzado:
- `Producto` representa el modelo general del articulo
- `VarianteProducto` representa una combinacion concreta vendible
- cada variante tiene su propio stock
- `Producto` se relaciona con `Marca`, `TipoProducto`, `Genero`, `Categoria` y `Usuario`

## Estado Frente A La Consigna

### Cumplido

- persistencia de datos con JPA/Hibernate
- definicion explicita de entidades y relaciones
- arquitectura en capas
- API REST para las entidades principales
- registro de usuarios
- login simple
- catalogo de productos
- categorias
- carrito
- checkout
- historial de ordenes
- manejo de errores
- validaciones basicas

### Parcial O Pendiente

- seguridad real de endpoints con autenticacion integrada
- roles y reglas de acceso
- asociar automaticamente operaciones al usuario autenticado
- password encriptada
- multiples imagenes por producto

Observacion:
- Spring Security ya esta incorporado como dependencia y existe una configuracion minima
- por ahora los endpoints se dejan abiertos para facilitar el desarrollo y las pruebas del TP

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring Security
- Hibernate
- H2 Database
- MySQL Connector
- Lombok
- Maven

## Modelo De Dominio

Principales entidades implementadas:
- `Usuario`
- `Categoria`
- `Marca`
- `TipoProducto`
- `Genero`
- `Producto`
- `VarianteProducto`
- `Carrito`
- `ItemCarrito`
- `Orden`
- `DetalleOrden`

Decision de modelado principal:
- `Producto` = modelo general del articulo
- `VarianteProducto` = unidad vendible concreta con atributos propios y stock propio

Esto permite manejar correctamente:
- talle
- color
- stock por variante
- carrito basado en una variante real
- descuento de stock preciso al momento del checkout

## Como Correr El Proyecto

### 1. Requisitos

- Java 17 instalado
- Maven Wrapper incluido en el proyecto

### 2. Levantar La Aplicacion

Desde la raiz del proyecto:

```bash
./mvnw spring-boot:run
```

La aplicacion queda disponible en:

```text
http://localhost:8080
```

Observacion:
- este proyecto no tiene frontend
- la raiz `/` no muestra una pagina web
- la API se prueba desde `/api/...`

### 3. Base De Datos H2

La configuracion actual usa H2 en memoria:

```text
jdbc:h2:mem:zapateria_db
```

Consola H2:

```text
http://localhost:8080/h2-console
```

Datos de acceso:
- JDBC URL: `jdbc:h2:mem:zapateria_db`
- User Name: `sa`
- Password: vacio

### 4. Ejecutar Tests

```bash
./mvnw test
```

## Como Probar La API

La API puede probarse de dos maneras:
- desde navegador para endpoints `GET`
- desde Postman para flujo completo

Base URL:

```text
http://localhost:8080/api
```

Ejemplos:

```text
GET http://localhost:8080/api/productos
GET http://localhost:8080/api/categorias
POST http://localhost:8080/api/login
POST http://localhost:8080/api/carrito?usuarioId=1
POST http://localhost:8080/api/carrito/checkout?usuarioId=1
```

## Documentacion Disponible

La carpeta `docs/` incluye documentacion para la entrega y para las pruebas:

- [API REST](./docs/api-rest.md)
- [Guia de pruebas](./docs/guia-pruebas.md)
- [Modelo draw.io](./docs/modelo-ecommerce.drawio)
- [Modelo DER draw.io](./docs/modelo-ecommerce-der.drawio)
- [Collection de Postman](./docs/postman-ecommerce-zapateria.json)

## Flujo Funcional Implementado

Un flujo completo posible dentro del sistema es:

1. Consultar productos del catalogo.
2. Ver variantes disponibles de un producto.
3. Registrar o identificar un usuario.
4. Agregar una variante al carrito.
5. Modificar o eliminar items del carrito.
6. Ejecutar checkout.
7. Validar stock.
8. Descontar stock de la variante.
9. Generar la orden.
10. Consultar historial de ordenes.

## Repositorio

Repositorio remoto del proyecto:

```text
https://github.com/NadiaTrotvilStudent/-E-Commerce-Zapateria.git
```
