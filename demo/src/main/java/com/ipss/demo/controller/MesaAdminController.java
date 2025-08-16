package com.ipss.demo.controller;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.repository.MesaRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/mesas")
@PreAuthorize("hasRole('ADMIN')")
public class MesaAdminController {

    private final MesaRepository repo;

    public MesaAdminController(MesaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Mesa> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Mesa crear(@RequestBody MesaDTO dto) {
        Mesa m = new Mesa();
        m.setNumero(dto.getNumero());
        m.setCapacidad(dto.getCapacidad());
        m.setUbicacion(dto.getUbicacion());
        m.setFumadores(Boolean.TRUE.equals(dto.getFumadores()));
        return repo.save(m);
    }

    @PutMapping("/{id}")
    public Mesa actualizar(@PathVariable Integer id, @RequestBody MesaDTO dto) {
        return repo.findById(id).map(m -> {
            m.setNumero(dto.getNumero());
            m.setCapacidad(dto.getCapacidad());
            m.setUbicacion(dto.getUbicacion());
            m.setFumadores(Boolean.TRUE.equals(dto.getFumadores()));
            return repo.save(m);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Descarga plantilla CSV
    @GetMapping("/plantilla")
    public ResponseEntity<Resource> plantilla() {
        String csv = "numero;capacidad;ubicacion;fumadores\n1;4;Terraza;false\n";
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mesas_plantilla.csv")
            .contentType(MediaType.TEXT_PLAIN)
            .contentLength(bytes.length)
            .body(new ByteArrayResource(bytes));
    }

    // Importar: texto pegado
    @PostMapping(path = "/import", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Map<String, Integer> importarTexto(@RequestBody String body) {
        return importarDesdeLineas(body);
    }

    // Importar: archivo CSV
    @PostMapping(path = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Integer> importarArchivo(@RequestPart("file") MultipartFile file) {
        try {
            return importarDesdeLineas(new String(file.getBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo leer el archivo", e);
        }
    }

    private Map<String, Integer> importarDesdeLineas(String body) {
        int ok = 0, fail = 0;
        List<String> lines = Arrays.stream(body.split("\\R"))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        if (!lines.isEmpty() && lines.get(0).toLowerCase().contains("numero;")) lines.remove(0);

        for (String ln : lines) {
            try {
                String[] p = ln.split(";", -1);
                Mesa m = new Mesa();
                m.setNumero(Integer.parseInt(p[0].trim()));
                m.setCapacidad(Integer.parseInt(p[1].trim()));
                m.setUbicacion(p[2].trim());
                m.setFumadores(Boolean.parseBoolean(p[3].trim()));
                repo.save(m);
                ok++;
            } catch (Exception ex) {
                fail++;
            }
        }
        Map<String, Integer> r = new HashMap<>();
        r.put("ok", ok); r.put("fail", fail);
        return r;
    }

    public static class MesaDTO {
        private Integer numero, capacidad;
        private String ubicacion;
        private Boolean fumadores;
        public Integer getNumero() { return numero; }
        public void setNumero(Integer numero) { this.numero = numero; }
        public Integer getCapacidad() { return capacidad; }
        public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
        public String getUbicacion() { return ubicacion; }
        public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
        public Boolean getFumadores() { return fumadores; }
        public void setFumadores(Boolean fumadores) { this.fumadores = fumadores; }
    }
}
