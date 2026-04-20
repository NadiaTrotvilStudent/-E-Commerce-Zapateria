package com.zapateria.ecommerce.config;

import com.zapateria.ecommerce.model.Categoria;
import com.zapateria.ecommerce.model.Genero;
import com.zapateria.ecommerce.model.Marca;
import com.zapateria.ecommerce.model.Producto;
import com.zapateria.ecommerce.model.TipoProducto;
import com.zapateria.ecommerce.model.Usuario;
import com.zapateria.ecommerce.model.VarianteProducto;
import com.zapateria.ecommerce.repository.CategoriaRepository;
import com.zapateria.ecommerce.repository.GeneroRepository;
import com.zapateria.ecommerce.repository.MarcaRepository;
import com.zapateria.ecommerce.repository.ProductoRepository;
import com.zapateria.ecommerce.repository.TipoProductoRepository;
import com.zapateria.ecommerce.repository.UsuarioRepository;
import com.zapateria.ecommerce.repository.VarianteProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    /**
     * Carga datos de ejemplo cuando la base esta vacia.
     * Esto sirve para probar rapido la API sin tener que insertar todo a mano.
     */
    @Bean
    CommandLineRunner initData(
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            MarcaRepository marcaRepository,
            TipoProductoRepository tipoProductoRepository,
            GeneroRepository generoRepository,
            VarianteProductoRepository varianteProductoRepository
    ) {
        return args -> {
            // Si ya hay datos, no vuelve a cargarlos para evitar duplicados.
            if (categoriaRepository.count() > 0
                    || usuarioRepository.count() > 0
                    || productoRepository.count() > 0
                    || marcaRepository.count() > 0
                    || tipoProductoRepository.count() > 0
                    || generoRepository.count() > 0
                    || varianteProductoRepository.count() > 0) {
                return;
            }

            Categoria deportivos = categoriaRepository.save(Categoria.builder().nombre("Deportivos").build());
            Categoria formales = categoriaRepository.save(Categoria.builder().nombre("Formales").build());
            Categoria casuales = categoriaRepository.save(Categoria.builder().nombre("Casuales").build());

            Marca nike = marcaRepository.save(Marca.builder().nombre("Nike").build());
            Marca adidas = marcaRepository.save(Marca.builder().nombre("Adidas").build());
            Marca briganti = marcaRepository.save(Marca.builder().nombre("Briganti").build());

            TipoProducto zapatilla = tipoProductoRepository.save(TipoProducto.builder().nombre("Zapatilla").build());
            TipoProducto zapato = tipoProductoRepository.save(TipoProducto.builder().nombre("Zapato").build());
            TipoProducto bota = tipoProductoRepository.save(TipoProducto.builder().nombre("Bota").build());

            Genero hombre = generoRepository.save(Genero.builder().nombre("Hombre").build());
            Genero mujer = generoRepository.save(Genero.builder().nombre("Mujer").build());
            Genero unisex = generoRepository.save(Genero.builder().nombre("Unisex").build());

            // Usuario inicial para crear productos y probar el login.
            Usuario admin = usuarioRepository.save(Usuario.builder()
                    .username("admin")
                    .email("admin@zapateria.com")
                    .password("1234")
                    .nombre("Admin")
                    .apellido("Zapateria")
                    .build());

            Producto runner = productoRepository.save(Producto.builder()
                    .nombre("Zapatilla Runner")
                    .descripcion("Zapatilla liviana ideal para uso diario.")
                    .precio(new BigDecimal("89999.99"))
                    .stock(15)
                    .imagenUrl("https://example.com/runner.jpg")
                    .marca(nike)
                    .tipoProducto(zapatilla)
                    .genero(unisex)
                    .categoria(deportivos)
                    .usuarioCreador(admin)
                    .build());

            Producto oxford = productoRepository.save(Producto.builder()
                    .nombre("Zapato Oxford")
                    .descripcion("Zapato formal de cuero para ocasiones especiales.")
                    .precio(new BigDecimal("129999.99"))
                    .stock(8)
                    .imagenUrl("https://example.com/oxford.jpg")
                    .marca(briganti)
                    .tipoProducto(zapato)
                    .genero(hombre)
                    .categoria(formales)
                    .usuarioCreador(admin)
                    .build());

            Producto urbana = productoRepository.save(Producto.builder()
                    .nombre("Zapatilla Urbana Street")
                    .descripcion("Modelo casual para uso urbano con buena amortiguacion.")
                    .precio(new BigDecimal("104999.99"))
                    .stock(10)
                    .imagenUrl("https://example.com/street.jpg")
                    .marca(adidas)
                    .tipoProducto(zapatilla)
                    .genero(mujer)
                    .categoria(casuales)
                    .usuarioCreador(admin)
                    .build());

            // Variantes reales de cada producto: cambian talle, color y stock.
            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(runner)
                    .talle("40")
                    .color("Negro")
                    .stock(5)
                    .build());
            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(runner)
                    .talle("41")
                    .color("Negro")
                    .stock(4)
                    .build());
            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(runner)
                    .talle("42")
                    .color("Blanco")
                    .stock(6)
                    .build());

            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(oxford)
                    .talle("40")
                    .color("Marron")
                    .stock(3)
                    .build());
            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(oxford)
                    .talle("41")
                    .color("Marron")
                    .stock(2)
                    .build());
            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(oxford)
                    .talle("42")
                    .color("Negro")
                    .stock(3)
                    .build());

            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(urbana)
                    .talle("36")
                    .color("Blanco")
                    .stock(4)
                    .build());
            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(urbana)
                    .talle("37")
                    .color("Rosa")
                    .stock(3)
                    .build());
            varianteProductoRepository.save(VarianteProducto.builder()
                    .producto(urbana)
                    .talle("38")
                    .color("Blanco")
                    .stock(3)
                    .build());
        };
    }
}
