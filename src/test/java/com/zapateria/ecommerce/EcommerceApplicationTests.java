package com.zapateria.ecommerce;

import com.zapateria.ecommerce.dto.AgregarItemCarritoRequest;
import com.zapateria.ecommerce.dto.CarritoResponse;
import com.zapateria.ecommerce.dto.CheckoutResponse;
import com.zapateria.ecommerce.dto.OrdenResponse;
import com.zapateria.ecommerce.exception.BadRequestException;
import com.zapateria.ecommerce.model.Producto;
import com.zapateria.ecommerce.model.Usuario;
import com.zapateria.ecommerce.repository.ProductoRepository;
import com.zapateria.ecommerce.repository.UsuarioRepository;
import com.zapateria.ecommerce.repository.VarianteProductoRepository;
import com.zapateria.ecommerce.service.CarritoService;
import com.zapateria.ecommerce.service.OrdenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de integracion basicos para validar que el contexto levanta
 * y que el flujo principal de checkout funciona correctamente.
 */
@SpringBootTest
class EcommerceApplicationTests {

	@Autowired
	private CarritoService carritoService;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private VarianteProductoRepository varianteProductoRepository;

	@Autowired
	private OrdenService ordenService;

	@Test
	void contextLoads() {
		// Verifica que Spring Boot pueda iniciar el contexto completo.
	}

	@Test
	void checkoutDescuentaStockYVacíaCarrito() {
		// Simula una compra exitosa y valida stock, carrito y orden generada.
		Usuario usuario = crearUsuarioDePrueba("checkout-ok");
		Producto producto = productoRepository.findAllByOrderByNombreAsc().get(0);
		var variante = varianteProductoRepository.findByProductoIdOrderByColorAscTalleAsc(producto.getId()).get(0);
		int stockInicial = producto.getStock();
		int stockInicialVariante = variante.getStock();

		CarritoResponse carrito = carritoService.agregarProducto(
				usuario.getId(),
				new AgregarItemCarritoRequest(variante.getId(), 2)
		);

		assertEquals(2, carrito.cantidadItems());

		CheckoutResponse checkout = carritoService.checkout(usuario.getId());
		Producto actualizado = productoRepository.findById(producto.getId()).orElseThrow();
		var varianteActualizada = varianteProductoRepository.findById(variante.getId()).orElseThrow();
		CarritoResponse carritoVacio = carritoService.obtenerCarritoPorUsuario(usuario.getId());
		OrdenResponse orden = ordenService.obtenerOrden(checkout.ordenId());

		assertEquals("Checkout realizado correctamente", checkout.mensaje());
		assertEquals(usuario.getId(), orden.usuarioId());
		assertEquals(2, checkout.cantidadItemsProcesados());
		assertEquals(stockInicial - 2, actualizado.getStock());
		assertEquals(stockInicialVariante - 2, varianteActualizada.getStock());
		assertEquals(0, carritoVacio.cantidadItems());
		assertTrue(carritoVacio.items().isEmpty());
		assertEquals(checkout.total(), orden.total());
		assertEquals(1, orden.detalles().size());
		assertEquals(2, orden.detalles().get(0).cantidad());
		assertEquals(variante.getId(), orden.detalles().get(0).varianteProductoId());
	}

	@Test
	void checkoutFallaSiElStockYaNoAlcanza() {
		// Simula un cambio de stock previo al checkout y espera un error controlado.
		Usuario usuario = crearUsuarioDePrueba("checkout-stock");
		Producto producto = productoRepository.findAllByOrderByNombreAsc().get(0);
		var variante = varianteProductoRepository.findByProductoIdOrderByColorAscTalleAsc(producto.getId()).get(0);
		int stockInicial = variante.getStock();

		carritoService.agregarProducto(
				usuario.getId(),
				new AgregarItemCarritoRequest(variante.getId(), stockInicial)
		);

		variante.setStock(stockInicial - 1);
		varianteProductoRepository.save(variante);

		BadRequestException exception = assertThrows(
				BadRequestException.class,
				() -> carritoService.checkout(usuario.getId())
		);

		assertTrue(exception.getMessage().contains("Stock insuficiente"));
	}

	// Crea usuarios de prueba con datos unicos para evitar choques entre tests.
	private Usuario crearUsuarioDePrueba(String sufijo) {
		return usuarioRepository.save(Usuario.builder()
				.username("user-" + sufijo)
				.email(sufijo + "@test.com")
				.password("1234")
				.nombre("Test")
				.apellido("User")
				.build());
	}
}
