package com.ipss.demo.controller;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {
  private final MesaService service;
  public MesaController(MesaService service) { this.service = service; }

  @GetMapping public List<Mesa> listar() { return service.listar(); }

  @GetMapping("/{id}")
  public ResponseEntity<Mesa> obtener(@PathVariable Long id) {
    Mesa m = service.buscar(id);
    return m == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(m);
  }

  @PostMapping
  public ResponseEntity<Mesa> crear(@Valid @RequestBody Mesa m) {
    Mesa creada = service.crear(m);
    return ResponseEntity.created(URI.create("/api/mesas/" + creada.getId())).body(creada);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Mesa> actualizar(@PathVariable Long id, @Valid @RequestBody Mesa m) {
    Mesa upd = service.actualizar(id, m);
    return upd == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(upd);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
