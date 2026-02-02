package com.tuproyecto.inventory.service;

import com.tuproyecto.inventory.dto.DatosDetallePedido;
import com.tuproyecto.inventory.dto.DatosRegistroPedido;
import com.tuproyecto.inventory.dto.DatosRespuestaPedido;
import com.tuproyecto.inventory.model.DetallePedido;
import com.tuproyecto.inventory.model.Pedido;
import com.tuproyecto.inventory.model.Producto;
import com.tuproyecto.inventory.repository.DetallePedidoRepository;
import com.tuproyecto.inventory.repository.PedidoRepository;
import com.tuproyecto.inventory.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository, DetallePedidoRepository detallePedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Transactional
    public Pedido registrarPedido(DatosRegistroPedido datos) { // Devolvemos la entidad para que el Controller la use

        // 1. Creamos el objeto Pedido principal
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setMesa(datos.mesa());
        pedido.setTotal(0.0); // Inicializamos en cero

        // Guardamos primero para tener un ID generado
        pedido = pedidoRepository.save(pedido);

        List<DetallePedido> detalles = new ArrayList<>();
        Double totalAcumulado = 0.0;

        // 2. Procesamos cada producto
        for (DatosDetallePedido item : datos.productos()) {
            int filasActualizadas = productoRepository.descontarStock(item.id(), item.cantidad());

            // Validación de Stock
            if (filasActualizadas == 0) {
                Producto producto = productoRepository.findById(item.id())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                throw new RuntimeException("No hay stock suficiente para: " + producto.getNombre() +
                        ". Solo quedan " + producto.getStock() + " unidades disponibles.");
            }
            Producto producto = productoRepository.findById(item.id()).get();

            // Restamos stock y creamos el detalle

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            detalles.add(detalle);
            totalAcumulado += detalle.getPrecioUnitario() * item.cantidad();
        }

        // 3. Guardamos todos los detalles y actualizamos el pedido con el total final
        detallePedidoRepository.saveAll(detalles);
        pedido.setTotal(totalAcumulado);

        // Retornamos el pedido ya completo (con ID y Total)
        return pedidoRepository.save(pedido);
    }
}