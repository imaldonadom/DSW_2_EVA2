package com.ipss.demo.controller;

import com.ipss.demo.model.Disponibilidad;
import com.ipss.demo.service.DisponibilidadService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/comensal")
public class ComensalDisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public ComensalDisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    // /comensal?fecha=2025-08-19   (si no envías fecha usa hoy)
    @GetMapping
    public String home(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        LocalDate f = (fecha != null) ? fecha : LocalDate.now();
        List<Disponibilidad> disponibles = disponibilidadService.listarPorFechaOrdenado(f);

        model.addAttribute("fecha", f);
        model.addAttribute("disponibles", disponibles);
        return "comensal/disponibilidad"; // templates/comensal/disponibilidad.html
    }
}
