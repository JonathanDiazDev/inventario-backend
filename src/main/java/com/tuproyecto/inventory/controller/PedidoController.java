package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.dto.DatosRegistroPedido;
import com.tuproyecto.inventory.dto.DatosRespuestaPedido;
import com.tuproyecto.inventory.model.Pedido;
import com.tuproyecto.inventory.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<DatosRespuestaPedido> registrar(@RequestBody DatosRegistroPedido datos, UriComponentsBuilder uriComponentsBuilder) {
        // 1. Llamamos al servicio y recibimos el objeto Pedido ya guardado
        Pedido pedido = pedidoService.registrarPedido(datos);

        // 2. Convertimos el pedido a un DTO de respuesta para no exponer la entidad pura
        DatosRespuestaPedido respuesta = new DatosRespuestaPedido(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getMesa(),
                pedido.getTotal()
        );

        // 3. Creamos la URL dinámica (ej: http://localhost:8080/api/pedidos/5)
        URI url = uriComponentsBuilder.path("/api/pedidos/{id}").buildAndExpand(pedido.getId()).toUri();

        // 4. Retornamos 201 Created, con la URL en el header y el DTO en el cuerpo
        return ResponseEntity.created(url).body(respuesta);
    }
}