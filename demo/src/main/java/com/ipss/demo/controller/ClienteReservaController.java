package com.ipss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/cliente")
public class ClienteReservaController {

    // ÚNICO dueño de GET /cliente
    @GetMapping({"", "/"})
    public String clienteHome(@RequestParam(required = false) LocalDate fecha, Model model) {
        model.addAttribute("titulo", "Panel Cliente");
        if (fecha != null) model.addAttribute("fecha", fecha);
        return "cliente/index";
    }

    // Ejemplo de subrutas: /cliente/reservas
    @GetMapping("/reservas")
    public String reservas(@RequestParam(required = false) LocalDate fecha, Model model) {
        model.addAttribute("titulo", "Mis Reservas");
        if (fecha != null) model.addAttribute("fecha", fecha);
        return "cliente/reservas";
    }
}
