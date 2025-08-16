package com.ipss.demo.controller;

import com.ipss.demo.model.AppUser;
import com.ipss.demo.model.Role;
import com.ipss.demo.repository.AppUserRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class AppUserAdminController {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AppUserAdminController(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // Descargar plantilla
    @GetMapping("/template")
    public ResponseEntity<Resource> plantillaUsuarios() {
        String csv = "username;password;role;enabled\n" +
                     "ana;1234;CLIENTE;true\n" +
                     "bruno;abcd;COMENSAL;true\n" +
                     "root;admin;ADMIN;true\n";
        ByteArrayResource res = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios_plantilla.csv")
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

        @GetMapping("/list") // quedará en: /api/usuarios/list
    public List<Map<String, Object>> listarUsuarios() {
        return repo.findAll().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId());
            m.put("username", u.getUsername());
            m.put("role", u.getRole().name());
            m.put("enabled", u.isEnabled());
            return m;
        }).toList();
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
            // Si primera fila parece cabecera, la saltamos
            String h = lines.get(0).toLowerCase();
            if (h.contains("username") && h.contains("password") && h.contains("role")) {
                lines = lines.subList(1, lines.size());
            }
        }

        for (String line : lines) {
            try {
                String[] p = line.split(";", -1);
                if (p.length < 4) throw new IllegalArgumentException("Fila incompleta: " + line);

                String username = p[0].trim();
                String password = p[1].trim();
                String roleStr   = p[2].trim().toUpperCase(Locale.ROOT);
                String enabledS  = p[3].trim();

                Role role = Role.valueOf(roleStr);                  // CLIENTE/COMENSAL/ADMIN
                boolean enabled = parseBool(enabledS);              // true/false/si/no/1/0

                AppUser u = repo.findByUsername(username).orElseGet(AppUser::new);
                u.setUsername(username);
                if (!password.isBlank()) {
                    u.setPassword(encoder.encode(password));
                } else if (u.getId() == null) {
                    throw new IllegalArgumentException("Password vacío para nuevo usuario");
                }
                u.setRole(role);
                u.setEnabled(enabled);

                repo.save(u);
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
