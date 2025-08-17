package com.ipss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    // Vista lista de mesas (HTML)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de Mesas");
        return "mesas/lista"; // src/main/resources/templates/mesas/lista.html o /static/mesas/lista.html
    }

    // Vista detalle de una mesa (HTML)
    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("titulo", "Detalle Mesa " + id);
        model.addAttribute("id", id);
        return "mesas/detalle"; // templates/mesas/detalle.html
    }
}
