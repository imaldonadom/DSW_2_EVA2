package com.ipss.demo.controller;

import com.ipss.demo.dto.CrearReservaDTO;
import com.ipss.demo.model.Reserva;
import com.ipss.demo.service.ReservaRules;
import com.ipss.demo.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaRules reservaRules;

    public ReservaController(ReservaService reservaService, ReservaRules reservaRules) {
        this.reservaService = reservaService;
        this.reservaRules = reservaRules;
    }

    // Crear reserva (CLIENTE o ADMIN)
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CrearReservaDTO dto) {
        try {
            // Valida horario laboral configurable
            reservaRules.validarHorario(dto.getInicio(), dto.getMinutos());

            // Lógica de creación (delegada al service)
            Reserva r = reservaService.crear(dto);
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Reservas del usuario autenticado
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @GetMapping("/mias")
    public ResponseEntity<?> misReservas(Principal principal) {
        String username = principal.getName(); // usuario autenticado
        return ResponseEntity.ok(reservaService.misReservas(username));
    }

    // Cancelar una reserva
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            reservaService.cancelar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Listado completo (para COMENSAL/ADMIN)
    @PreAuthorize("hasAnyRole('COMENSAL','ADMIN')")
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        return ResponseEntity.ok(reservaService.listarTodas());
    }
}
