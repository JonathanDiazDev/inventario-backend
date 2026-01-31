package com.tuproyecto.inventory.controller;

import com.tuproyecto.inventory.service.MovimientoService;
import com.tuproyecto.inventory.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class MovimientoController  {

    @Autowired
    private MovimientoService movimientoService;
}
