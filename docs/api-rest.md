# API REST - E-Commerce Zapateria

Esta documentacion resume los endpoints REST disponibles actualmente en el proyecto.

Base path general:

```text
/api
```

## Resumen General

| Recurso | Endpoint base | Estado actual |
|---|---|---|
| Auth | `/api/login` | Login simple |
| Usuarios | `/api/usuarios` | Alta y listado |
| Categorias | `/api/categorias` | Alta y listado |
| Marcas | `/api/marcas` | Solo listado |
| Generos | `/api/generos` | Solo listado |
| Tipos de producto | `/api/tipos-producto` | Solo listado |
| Productos | `/api/productos` | CRUD completo |
| Variantes | `/api/productos/{productoId}/variantes` | Alta y listado |
| Carrito | `/api/carrito` | API de negocio |
| Ordenes | `/api/ordenes` | Consulta de historial |

## Auth

### `POST /api/login`

Valida email y password de un usuario.

Observacion:
- hoy el login es simple y valida credenciales
- todavia no genera sesion ni token
- por eso la autenticacion real de endpoints aun no esta implementada

**Body**

```json
{
  "email": "admin@zapateria.com",
  "password": "1234"
}
```

**Respuesta**

```json
{
  "mensaje": "Login correcto",
  "usuario": {
    "id": 1,
    "username": "admin",
    "email": "admin@zapateria.com",
    "nombre": "Admin",
    "apellido": "Zapateria"
  }
}
```

## Usuarios

### `GET /api/usuarios`

Lista todos los usuarios registrados.

### `POST /api/usuarios`

Registra un usuario nuevo.

Observacion:
- este endpoint debe seguir siendo publico, porque se usa para registracion

**Body**

```json
{
  "username": "juanperez",
  "email": "juan@mail.com",
  "password": "1234",
  "nombre": "Juan",
  "apellido": "Perez"
}
```

## Categorias

### `GET /api/categorias`

Lista todas las categorias.

Observacion:
- este endpoint puede considerarse publico porque forma parte del catalogo

### `POST /api/categorias`

Crea una categoria nueva.

**Body**

```json
{
  "nombre": "Casuales"
}
```

## Marcas

### `GET /api/marcas`

Lista todas las marcas disponibles.

Observacion:
- este endpoint puede considerarse publico porque sirve para mostrar filtros del catalogo

## Generos

### `GET /api/generos`

Lista todos los generos disponibles.

Observacion:
- este endpoint puede considerarse publico porque sirve para mostrar filtros del catalogo

## Tipos De Producto

### `GET /api/tipos-producto`

Lista los tipos de producto.

Ejemplos:
- `Zapatilla`
- `Bota`
- `Cinto`
- `Sandalia`

Observacion:
- este endpoint puede considerarse publico porque sirve para mostrar filtros del catalogo

## Productos

### `GET /api/productos`

Lista todos los productos ordenados alfabeticamente.

Parametro opcional:

```text
categoriaId
```

Ejemplo:

```text
GET /api/productos?categoriaId=1
```

Observacion:
- este endpoint deberia ser publico
- hoy ya puede consultarse sin login

### `GET /api/productos/{id}`

Devuelve el detalle de un producto.

Observacion:
- este endpoint deberia ser publico
- hoy ya puede consultarse sin login

### `POST /api/productos`

Crea un producto nuevo.

**Body**

```json
{
  "nombre": "Zapatilla Runner Pro",
  "descripcion": "Modelo liviano para uso diario y entrenamiento.",
  "precio": 89999.99,
  "stock": 0,
  "imagenUrl": "https://example.com/runner-pro.jpg",
  "marcaId": 1,
  "tipoProductoId": 1,
  "generoId": 3,
  "categoriaId": 1,
  "usuarioCreadorId": 1
}
```

Nota:
- el `stock` del producto funciona como stock total agregado
- el stock real por combinacion se maneja desde las variantes

Observacion:
- en una version con seguridad real, este endpoint deberia requerir autenticacion
- hoy todavia no esta protegido con login real

### `PUT /api/productos/{id}`

Actualiza los datos generales del producto.

Usa el mismo body que el alta.

Observacion:
- en una version con seguridad real, este endpoint deberia requerir autenticacion
- hoy todavia no esta protegido con login real

### `DELETE /api/productos/{id}`

Elimina un producto.

Observacion:
- en una version con seguridad real, este endpoint deberia requerir autenticacion
- hoy todavia no esta protegido con login real

## Variantes De Producto

### `GET /api/productos/{productoId}/variantes`

Lista las variantes de un producto.

Observacion:
- este endpoint deberia ser publico para poder mostrar talle, color y stock al cliente

### `POST /api/productos/{productoId}/variantes`

Crea una variante para un producto existente.

**Body**

```json
{
  "talle": "42",
  "color": "Negro",
  "stock": 5
}
```

Cada variante representa una unidad vendible concreta del producto:
- `talle`
- `color`
- `stock`

Observacion:
- en una version con seguridad real, este endpoint deberia requerir autenticacion
- hoy todavia no esta protegido con login real

## Carrito

### `GET /api/carrito?usuarioId={usuarioId}`

Devuelve el carrito actual del usuario.

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

### `POST /api/carrito?usuarioId={usuarioId}`

Agrega una variante al carrito.

**Body**

```json
{
  "varianteProductoId": 1,
  "cantidad": 2
}
```

Importante:
- el carrito trabaja con `varianteProductoId`
- no agrega el producto general, sino la variante exacta elegida

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

### `PATCH /api/carrito/items/{itemId}?usuarioId={usuarioId}`

Actualiza la cantidad de un item ya cargado en el carrito.

**Body**

```json
{
  "cantidad": 3
}
```

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

### `DELETE /api/carrito/items/{itemId}?usuarioId={usuarioId}`

Elimina un item especifico del carrito.

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

### `DELETE /api/carrito?usuarioId={usuarioId}`

Vacía completamente el carrito.

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

### `POST /api/carrito/checkout?usuarioId={usuarioId}`

Realiza el checkout del carrito:
- valida stock por variante
- descuenta stock
- genera la orden
- vacia el carrito

**Respuesta esperada**

```json
{
  "mensaje": "Checkout realizado correctamente",
  "ordenId": 1,
  "carritoId": 1,
  "cantidadItemsProcesados": 2,
  "total": 179999.98
}
```

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

## Ordenes

### `GET /api/ordenes?usuarioId={usuarioId}`

Lista el historial de ordenes de un usuario.

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

### `GET /api/ordenes/{ordenId}`

Devuelve el detalle de una orden especifica.

Observacion:
- conceptualmente este endpoint deberia requerir login
- hoy todavia no esta protegido con autenticacion real

## Flujo Recomendado De Uso

1. Registrar usuario con `POST /api/usuarios`
2. Hacer login con `POST /api/login`
3. Consultar catalogos:
   - `GET /api/categorias`
   - `GET /api/marcas`
   - `GET /api/generos`
   - `GET /api/tipos-producto`
4. Ver productos con `GET /api/productos`
5. Ver variantes de un producto con `GET /api/productos/{productoId}/variantes`
6. Agregar una variante al carrito con `POST /api/carrito`
7. Consultar carrito con `GET /api/carrito`
8. Confirmar compra con `POST /api/carrito/checkout`
9. Ver historial con `GET /api/ordenes?usuarioId=...`

## Notas De Modelado

- `Producto` representa el modelo general del articulo.
- `VarianteProducto` representa la combinacion concreta vendible.
- El carrito y la orden trabajan con variantes.
- El stock real se descuenta desde `VarianteProducto`.
- El `stock` de `Producto` representa el stock total agregado de todas sus variantes.

## Observacion Sobre Seguridad

Actualmente la API no tiene autenticacion real aplicada a los endpoints.

Eso significa que:
- existe un login simple
- las credenciales se validan
- pero todavia no se genera sesion, token ni contexto autenticado en Spring Security

Regla de negocio esperada para una siguiente etapa:
- el catalogo deberia ser publico
- el carrito, checkout, ordenes y gestion de productos deberian requerir autenticacion

## Pendientes / Mejoras Futuras

Estas mejoras no forman parte de la implementacion actual, pero son la evolucion natural del proyecto:

### 1. Seguridad real

- implementar autenticacion real con Spring Security
- generar sesion o token luego del login
- dejar de depender solo de validacion manual de email y password

### 2. Roles

- incorporar roles en usuario, por ejemplo:
  - `CLIENTE`
  - `VENDEDOR`
  - `ADMIN`
- usar esos roles para definir que operaciones puede hacer cada tipo de usuario

### 3. Proteccion de endpoints

- dejar publicos solamente los endpoints de catalogo:
  - productos
  - detalle de producto
  - categorias
  - marcas
  - generos
  - tipos de producto
  - variantes de producto
- proteger los endpoints de carrito, checkout, ordenes y gestion de productos

### 4. Validacion por usuario autenticado

- evitar recibir `usuarioId` por request param en operaciones sensibles
- tomar el usuario directamente desde el contexto autenticado
- impedir que un usuario consulte o modifique informacion de otro usuario

### 5. Multiples imagenes por producto

- permitir que un producto tenga mas de una imagen
- modelar una entidad adicional, por ejemplo `ImagenProducto`
- usar esa mejora para mostrar galeria en el detalle del producto

### 6. Mejoras complementarias posibles

- agregar actualizacion y eliminacion de variantes
- permitir filtros mas avanzados de catalogo
- agregar historial mas completo de ordenes con estados
- incorporar paginacion en listados
