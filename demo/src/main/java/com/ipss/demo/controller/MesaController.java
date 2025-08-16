// Ruta: src/main/java/com/ipss/demo/controller/MesaController.java
package com.ipss.demo.controller;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.service.MesaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService service;

    public MesaController(MesaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Mesa> listar() {
        return service.listar();
    }

    // (Opcional) otros GET públicos… pero nada de POST/PUT/DELETE aquí
}
