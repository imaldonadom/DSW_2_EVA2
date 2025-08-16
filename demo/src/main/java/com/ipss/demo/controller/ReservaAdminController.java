package com.ipss.demo.controller;

import com.ipss.demo.dto.CrearReservaDTO;
import com.ipss.demo.model.Reserva;
import com.ipss.demo.repository.ReservaRepository;
import com.ipss.demo.service.ReservaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reservas")
@PreAuthorize("hasRole('ADMIN')")
public class ReservaAdminController {

    private final ReservaService service;
    private final ReservaRepository repo;

    public ReservaAdminController(ReservaService service, ReservaRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    /**
     * Lista las reservas/bloques de una mesa dentro de un rango [desde, hasta).
     * Ejemplo:
     *   GET /api/admin/reservas/dia?mesaId=1&desde=2025-08-16T00:00:00&hasta=2025-08-16T23:59:59
     */
    @GetMapping("/dia")
    public List<Reserva> reservasDelDia(
            @RequestParam Integer mesaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        return repo.findByMesaIdAndInicioBetween(mesaId, desde, hasta);
    }

    /**
     * Crea un bloqueo/reserva como ADMIN.
     * Usa el mismo DTO de creación que el cliente (mesaId, inicio, minutos).
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearReservaDTO dto) {
        try {
            Reserva r = service.crear(dto);
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Cancela una reserva por id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        return repo.findById(id)
                .map(r -> {
                    service.cancelar(r.getId());
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
