package com.ipss.demo.controller;

import com.ipss.demo.model.Mesa;
import com.ipss.demo.repository.MesaRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mesas")
public class MesaAdminController {

    private final MesaRepository repo;

    public MesaAdminController(MesaRepository repo) {
        this.repo = repo;
    }

    // Descargar plantilla
    @GetMapping("/template")
    public ResponseEntity<Resource> plantillaMesas() {
        String csv = "numero;capacidad;ubicacion;fumadores\n" +
                     "1;4;Terraza;false\n" +
                     "2;6;Interior;true\n";
        ByteArrayResource res = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mesas_plantilla.csv")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .contentLength(res.contentLength())
                .body(res);
    }

    // Importar desde TEXTO
    @PostMapping(value = "/import-text", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Map<String, Object> importarTexto(@RequestBody String body) {
        return importarLineas(parseLines(body));
    }

    // Importar desde ARCHIVO
    @PostMapping(value = "/import-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> importarArchivo(@RequestPart("file") MultipartFile file) throws Exception {
        String text = new String(file.getBytes(), StandardCharsets.UTF_8);
        return importarLineas(parseLines(text));
    }

    private List<String> parseLines(String text) {
        if (text == null) return List.of();
        return Arrays.stream(text.replace("\r", "").split("\n"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private Map<String, Object> importarLineas(List<String> lines) {
        int ok = 0, fail = 0;
        List<String> errores = new ArrayList<>();

        if (!lines.isEmpty()) {
            // Saltar cabecera si corresponde
            String h = lines.get(0).toLowerCase();
            if (h.contains("numero") && h.contains("capacidad") && h.contains("ubicacion")) {
                lines = lines.subList(1, lines.size());
            }
        }

        for (String line : lines) {
            try {
                String[] p = line.split(";", -1);
                if (p.length < 4) throw new IllegalArgumentException("Fila incompleta: " + line);

                Integer numero    = Integer.valueOf(p[0].trim());
                Integer capacidad = Integer.valueOf(p[1].trim());
                String ubicacion  = p[2].trim();
                boolean fumadores = parseBool(p[3].trim());

                // Si ya existe por "numero", actualiza; si no, crea
                Mesa m = repo.findAll().stream()
                        .filter(x -> Objects.equals(x.getNumero(), numero))
                        .findFirst()
                        .orElseGet(Mesa::new);

                m.setNumero(numero);
                m.setCapacidad(capacidad);
                m.setUbicacion(ubicacion);
                m.setFumadores(fumadores);

                repo.save(m);
                ok++;
            } catch (Exception ex) {
                fail++;
                errores.add(line + " -> " + ex.getMessage());
            }
        }
        Map<String, Object> r = new HashMap<>();
        r.put("ok", ok);
        r.put("fail", fail);
        r.put("errors", errores);
        return r;
    }

    private boolean parseBool(String s) {
        String x = s.toLowerCase(Locale.ROOT);
        return x.equals("true") || x.equals("1") || x.equals("si") || x.equals("sí");
    }
}
