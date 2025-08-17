package com.ipss.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/mesas")
public class MesaApi {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar() {
        List<Map<String, Object>> out = new ArrayList<>();
        Map<String, Object> a = new HashMap<>(); a.put("id", 1); a.put("nombre", "Mesa 1"); a.put("capacidad", 4);
        Map<String, Object> b = new HashMap<>(); b.put("id", 2); b.put("nombre", "Mesa 2"); b.put("capacidad", 2);
        out.add(a); out.add(b);
        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        Map<String, Object> mesa = new HashMap<>();
        mesa.put("id", id); mesa.put("nombre", "Mesa " + id); mesa.put("capacidad", 4);
        return ResponseEntity.ok(mesa);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Map<String, Object> body) {
        body.putIfAbsent("id", 99);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        body.put("id", id);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patch(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        body.put("id", id);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }
}
