# Guia De Pruebas - E-Commerce Zapateria

Esta guia sirve para probar el proyecto completo de forma manual, con foco principal en `Postman`:
- levantar la aplicacion
- ver las tablas en H2
- probar la API endpoint por endpoint en Postman
- recorrer el flujo de catalogo, carrito y checkout

## 1. Requisitos Previos

Antes de empezar, verificar:
- Java 17 instalado
- Maven Wrapper disponible en el proyecto
- Postman instalado

## 2. Levantar La Aplicacion

Pararse en la raiz del proyecto y ejecutar:

```bash
./mvnw spring-boot:run
```

Si todo levanta bien, la aplicacion queda disponible en:

```text
http://localhost:8080
```

## 3. Abrir La Consola H2

Entrar en el navegador a:

```text
http://localhost:8080/h2-console
```

Usar estos datos:
- JDBC URL: `jdbc:h2:mem:zapateria_db`
- User Name: `sa`
- Password: dejar vacio

Luego presionar `Connect`.

## 4. Tablas Esperadas

Una vez dentro de H2, deberian existir tablas como estas:
- `USUARIOS`
- `CATEGORIAS`
- `MARCAS`
- `TIPOS_PRODUCTO`
- `GENEROS`
- `PRODUCTOS`
- `VARIANTES_PRODUCTO`
- `CARRITOS`
- `ITEMS_CARRITO`
- `ORDENES`
- `DETALLES_ORDEN`

## 5. Consultas SQL Utiles

Estas consultas ayudan a ver el estado de la base mientras se prueba la API:

```sql
SELECT * FROM USUARIOS;
SELECT * FROM CATEGORIAS;
SELECT * FROM MARCAS;
SELECT * FROM TIPOS_PRODUCTO;
SELECT * FROM GENEROS;
SELECT * FROM PRODUCTOS;
SELECT * FROM VARIANTES_PRODUCTO;
SELECT * FROM CARRITOS;
SELECT * FROM ITEMS_CARRITO;
SELECT * FROM ORDENES;
SELECT * FROM DETALLES_ORDEN;
```

## 6. Datos Iniciales

Cuando la base esta vacia, el proyecto carga datos de ejemplo automaticamente.

Usuario inicial para pruebas:

```json
{
  "email": "admin@zapateria.com",
  "password": "1234"
}
```

Observacion:
- como la base actual es `H2 en memoria`, los datos se pierden al apagar la aplicacion
- al volver a levantarla, se vuelven a cargar los datos de prueba

## 7. Probar La API En Postman

Usar como base:

```text
http://localhost:8080/api
```

Sugerencia:
- crear una collection llamada `E-Commerce Zapateria`
- dentro de la collection crear una variable `baseUrl`
- asignar como valor:

```text
http://localhost:8080/api
```

De esa manera, en cada request se puede usar:

```text
{{baseUrl}}
```

Ejemplo:

```text
{{baseUrl}}/productos
```

### 7.1. Configuracion Basica En Postman

Para todos los requests con body JSON:
- ir a la pestaña `Body`
- elegir `raw`
- elegir tipo `JSON`

Para requests `POST`, `PUT` y `PATCH` con JSON:
- Postman normalmente agrega solo el header
- si no lo hace, usar:

```text
Content-Type: application/json
```

### 7.1. Como Importar La Collection En Postman

La collection ya esta incluida en el proyecto en:

- [postman-ecommerce-zapateria.json](./postman-ecommerce-zapateria.json)

Paso a paso:

1. Abrir Postman.
2. Presionar `Import`.
3. Elegir `Upload Files`.
4. Seleccionar el archivo `docs/postman-ecommerce-zapateria.json`.
5. Confirmar la importacion.

Resultado esperado:
- aparece una collection llamada `E-Commerce Zapateria`

### 7.2. Como Configurar Las Variables En Postman

Una vez importada la collection:

1. Hacer click sobre la collection `E-Commerce Zapateria`.
2. Ir a la pestaña `Variables`.
3. Verificar o cargar estos valores:

```text
baseUrl = http://localhost:8080/api
usuarioId = 1
productoId = 1
varianteProductoId = 1
itemId = 1
ordenId = 1
categoriaId = 1
```

Observacion:
- `itemId` y `ordenId` pueden cambiar durante las pruebas
- despues de crear un item o una orden, conviene actualizar esas variables con el id real devuelto por la API

### 7.3. Orden Recomendado Dentro De Postman

Para no perderse, conviene probar la collection en este orden:

1. `Catalogo > Listar productos`
2. `Auth > Login simple`
3. `Catalogo > Listar variantes de producto`
4. `Carrito > Obtener carrito`
5. `Carrito > Agregar item al carrito`
6. `Carrito > Checkout`
7. `Ordenes > Listar ordenes por usuario`
8. `Ordenes > Detalle de orden`

### 7.4. Verificar Que La API Responde

Request:

```http
GET /api/productos
```

URL completa:

```text
http://localhost:8080/api/productos
```

Resultado esperado:
- lista de productos cargados por el `DataLoader`

### 7.5. Listar Usuarios

```http
GET /api/usuarios
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/usuarios`

### 7.6. Probar Login Simple

```http
POST /api/login
Content-Type: application/json
```

Body:

```json
{
  "email": "admin@zapateria.com",
  "password": "1234"
}
```

Resultado esperado:
- mensaje de login correcto
- datos del usuario

Observacion:
- hoy el login no genera token ni sesion
- por ahora solo valida credenciales

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/login`
- Body:

```json
{
  "email": "admin@zapateria.com",
  "password": "1234"
}
```

### 7.7. Registrar Un Usuario Nuevo

```http
POST /api/usuarios
Content-Type: application/json
```

Body:

```json
{
  "username": "juanperez",
  "email": "juan@mail.com",
  "password": "1234",
  "nombre": "Juan",
  "apellido": "Perez"
}
```

Despues se puede verificar con:

```http
GET /api/usuarios
```

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/usuarios`
- Body:

```json
{
  "username": "juanperez",
  "email": "juan@mail.com",
  "password": "1234",
  "nombre": "Juan",
  "apellido": "Perez"
}
```

## 8. Probar Catalogo

### 8.1. Listar Categorias

```http
GET /api/categorias
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/categorias`

### 8.2. Listar Marcas

```http
GET /api/marcas
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/marcas`

### 8.3. Listar Generos

```http
GET /api/generos
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/generos`

### 8.4. Listar Tipos De Producto

```http
GET /api/tipos-producto
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/tipos-producto`

### 8.5. Listar Todos Los Productos

```http
GET /api/productos
```

Resultado esperado:
- productos ordenados alfabeticamente

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/productos`

### 8.6. Filtrar Productos Por Categoria

Ejemplo:

```http
GET /api/productos?categoriaId=1
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/productos?categoriaId=1`

### 8.7. Ver Detalle De Un Producto

Ejemplo:

```http
GET /api/productos/1
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/productos/1`

### 8.8. Ver Variantes De Un Producto

Ejemplo:

```http
GET /api/productos/1/variantes
```

Resultado esperado:
- lista de variantes con `id`, `talle`, `color` y `stock`

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/productos/1/variantes`

## 9. Crear Datos Nuevos

### 9.1. Crear Un Producto

```http
POST /api/productos
Content-Type: application/json
```

Body:

```json
{
  "nombre": "Bota Trek",
  "descripcion": "Bota resistente para uso urbano y exterior.",
  "precio": 154999.99,
  "stock": 0,
  "imagenUrl": "https://example.com/bota-trek.jpg",
  "marcaId": 1,
  "tipoProductoId": 3,
  "generoId": 3,
  "categoriaId": 1,
  "usuarioCreadorId": 1
}
```

Observacion:
- el `stock` del producto queda como total general
- el stock real vendible se maneja por variante

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/productos`
- Body:

```json
{
  "nombre": "Bota Trek",
  "descripcion": "Bota resistente para uso urbano y exterior.",
  "precio": 154999.99,
  "stock": 0,
  "imagenUrl": "https://example.com/bota-trek.jpg",
  "marcaId": 1,
  "tipoProductoId": 3,
  "generoId": 3,
  "categoriaId": 1,
  "usuarioCreadorId": 1
}
```

### 9.2. Crear Una Variante Para Un Producto

Ejemplo para el producto `1`:

```http
POST /api/productos/1/variantes
Content-Type: application/json
```

Body:

```json
{
  "talle": "43",
  "color": "Azul",
  "stock": 7
}
```

Luego verificar con:

```http
GET /api/productos/1/variantes
```

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/productos/1/variantes`
- Body:

```json
{
  "talle": "43",
  "color": "Azul",
  "stock": 7
}
```

Tambien conviene mirar en H2:

```sql
SELECT * FROM VARIANTES_PRODUCTO;
```

## 10. Probar Carrito

Importante:
- hoy el carrito trabaja con `VarianteProducto`
- no se agrega el `Producto` general, sino una variante puntual

### 10.1. Ver El Carrito Del Usuario

Ejemplo:

```http
GET /api/carrito?usuarioId=1
```

Si no existe carrito, el sistema lo crea automaticamente.

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/carrito?usuarioId=1`

### 10.2. Agregar Una Variante Al Carrito

```http
POST /api/carrito?usuarioId=1
Content-Type: application/json
```

Body:

```json
{
  "varianteProductoId": 1,
  "cantidad": 2
}
```

Resultado esperado:
- aparece un item en el carrito
- se informa producto, variante, cantidad y subtotal

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/carrito?usuarioId=1`
- Body:

```json
{
  "varianteProductoId": 1,
  "cantidad": 2
}
```

Despues verificar en H2:

```sql
SELECT * FROM CARRITOS;
SELECT * FROM ITEMS_CARRITO;
```

### 10.3. Cambiar La Cantidad De Un Item

Primero obtener el `itemId` consultando el carrito.

Luego ejecutar:

```http
PATCH /api/carrito/items/1?usuarioId=1
Content-Type: application/json
```

Body:

```json
{
  "cantidad": 3
}
```

En Postman:
- Method: `PATCH`
- URL: `{{baseUrl}}/carrito/items/1?usuarioId=1`
- Body:

```json
{
  "cantidad": 3
}
```

### 10.4. Eliminar Un Item Del Carrito

```http
DELETE /api/carrito/items/1?usuarioId=1
```

En Postman:
- Method: `DELETE`
- URL: `{{baseUrl}}/carrito/items/1?usuarioId=1`

### 10.5. Vaciar El Carrito

```http
DELETE /api/carrito?usuarioId=1
```

En Postman:
- Method: `DELETE`
- URL: `{{baseUrl}}/carrito?usuarioId=1`

## 11. Probar Checkout

### 11.1. Agregar Nuevamente Un Item Al Carrito

```http
POST /api/carrito?usuarioId=1
Content-Type: application/json
```

Body:

```json
{
  "varianteProductoId": 1,
  "cantidad": 1
}
```

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/carrito?usuarioId=1`
- Body:

```json
{
  "varianteProductoId": 1,
  "cantidad": 1
}
```

### 11.2. Ejecutar Checkout

```http
POST /api/carrito/checkout?usuarioId=1
```

Resultado esperado:
- mensaje de checkout correcto
- id de orden generada
- total de la compra
- cantidad de items procesados

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/carrito/checkout?usuarioId=1`

### 11.3. Verificar Los Efectos Del Checkout

Consultar en H2:

```sql
SELECT * FROM ITEMS_CARRITO;
SELECT * FROM ORDENES;
SELECT * FROM DETALLES_ORDEN;
SELECT * FROM VARIANTES_PRODUCTO;
SELECT * FROM PRODUCTOS;
```

Se deberia ver:
- el carrito vacio
- una nueva orden creada
- detalles de la orden guardados
- stock descontado en la variante comprada
- stock total del producto recalculado

## 12. Probar Historial De Ordenes

### 12.1. Listar Ordenes De Un Usuario

```http
GET /api/ordenes?usuarioId=1
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/ordenes?usuarioId=1`

### 12.2. Ver El Detalle De Una Orden

Ejemplo:

```http
GET /api/ordenes/1
```

En Postman:
- Method: `GET`
- URL: `{{baseUrl}}/ordenes/1`

## 13. Casos De Error Recomendados

Tambien conviene probar errores para validar que la API responde bien.

### 13.1. Login Invalido

```http
POST /api/login
```

```json
{
  "email": "admin@zapateria.com",
  "password": "incorrecta"
}
```

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/login`
- Body:

```json
{
  "email": "admin@zapateria.com",
  "password": "incorrecta"
}
```

### 13.2. Registrar Usuario Con Email Repetido

Intentar crear dos veces el mismo usuario.

### 13.3. Agregar Al Carrito Una Variante Sin Stock Suficiente

Ejemplo:

```http
POST /api/carrito?usuarioId=1
```

```json
{
  "varianteProductoId": 1,
  "cantidad": 999
}
```

Resultado esperado:
- error de negocio indicando stock insuficiente

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/carrito?usuarioId=1`
- Body:

```json
{
  "varianteProductoId": 1,
  "cantidad": 999
}
```

### 13.4. Checkout Con Carrito Vacio

```http
POST /api/carrito/checkout?usuarioId=1
```

Resultado esperado:
- error indicando que el carrito esta vacio

En Postman:
- Method: `POST`
- URL: `{{baseUrl}}/carrito/checkout?usuarioId=1`

## 14. Flujo Completo Recomendado Para Demo

Si queres mostrar el proyecto de punta a punta, este es un buen orden:

1. Abrir H2 Console y mostrar tablas.
2. Hacer `GET /api/productos`.
3. Hacer `GET /api/productos/1/variantes`.
4. Hacer `POST /api/login`.
5. Hacer `GET /api/carrito?usuarioId=1`.
6. Hacer `POST /api/carrito?usuarioId=1`.
7. Mostrar en H2 que se creo el item del carrito.
8. Hacer `POST /api/carrito/checkout?usuarioId=1`.
9. Mostrar en H2 que se creo la orden y bajo el stock.
10. Hacer `GET /api/ordenes?usuarioId=1`.

## 15. Observaciones Importantes

- Hoy los endpoints estan abiertos para simplificar el desarrollo y las pruebas.
- El login actual es simple y no protege endpoints.
- La identificacion del usuario hoy se pasa por `usuarioId`.
- La base H2 actual es temporal porque corre en memoria.
- El modelo real de venta usa `VarianteProducto`, no `Producto` directo.

## 16. Documentacion Relacionada

Para complementar esta guia:
- [api-rest.md](./api-rest.md)
- [modelo-ecommerce.drawio](./modelo-ecommerce.drawio)
- [modelo-ecommerce-der.drawio](./modelo-ecommerce-der.drawio)
