package com.tuproyecto.inventory.dto;

import java.util.List;

public record DatosRegistroPedido(
        String mesa,                            //Numero de mesa
        List <DatosDetallePedido> productos     //Lista de que cosas

) {
}
