package com.tuproyecto.inventory.service;
import com.tuproyecto.inventory.dto.DatosRegistroProducto;
import com.tuproyecto.inventory.model.Categoria;
import com.tuproyecto.inventory.model.Producto;
import com.tuproyecto.inventory.repository.CategoriaRepository;
import com.tuproyecto.inventory.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Producto registrarProducto(DatosRegistroProducto datos) {


        Categoria categoria = categoriaRepository.findById(datos.categoriaId())
                .orElseThrow(()-> new RuntimeException("Categoria no encontrada"));

        Producto producto = new Producto(datos);

        producto.setCategoria(categoria);


        return productoRepository.save(producto);
    }


}
