package com.ipss.demo.controller;

import com.ipss.demo.dto.CrearDisponibilidadDTO;
import com.ipss.demo.model.Disponibilidad;
import com.ipss.demo.service.DisponibilidadService;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin/disponibilidades")
@CrossOrigin(origins="*")
public class AdminDisponibilidadController {
    private final DisponibilidadService service;
    public AdminDisponibilidadController(DisponibilidadService service){ this.service = service; }

    @GetMapping("/mesa/{mesaId}")
    public List<Disponibilidad> listar(@PathVariable Long mesaId, @RequestParam String fecha){
        return service.listarPorMesaYFecha(mesaId, LocalDate.parse(fecha));
    }

    @PostMapping
    public Disponibilidad crear(@RequestBody CrearDisponibilidadDTO dto){
        return service.crear(dto.mesaId(), dto.fecha(), dto.horaInicio(), dto.horaFin());
    }

    @PatchMapping("/{id}/estado")
    public Disponibilidad cambiarEstado(@PathVariable Long id, @RequestParam Disponibilidad.Estado estado){
        return service.cambiarEstado(id, estado);
    }
}
