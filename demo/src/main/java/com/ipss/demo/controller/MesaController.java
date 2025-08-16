package com.ipss.demo.controller;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.service.MesaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService service;

    public MesaController(MesaService service) {
        this.service = service;
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<Mesa> listar() {
        return service.listar();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> buscar(@PathVariable Integer id) {
        Mesa m = service.buscar(id);
        return (m == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(m);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Mesa crear(@RequestBody Mesa m) {
        return service.crear(m);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> actualizar(@PathVariable Integer id, @RequestBody Mesa m) {
        Mesa actualizado = service.actualizar(id, m);
        return (actualizado == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
