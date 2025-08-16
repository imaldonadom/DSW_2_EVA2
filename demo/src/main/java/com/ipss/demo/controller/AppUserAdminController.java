package com.ipss.demo.controller;

import com.ipss.demo.model.AppUser;
import com.ipss.demo.model.Role;
import com.ipss.demo.repository.AppUserRepository;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class AppUserAdminController {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AppUserAdminController(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // ---- DTOs / Views
    public record CreateUserDTO(String username, String password, Role role, Boolean enabled) {}
    public record RoleDTO(Role role) {}
    public record EnabledDTO(Boolean enabled) {}
    public record UserView(Long id, String username, Role role, boolean enabled) {}
    private UserView view(AppUser u) { return new UserView(u.getId(), u.getUsername(), u.getRole(), u.isEnabled()); }

    // ---- CRUD básico
    @GetMapping
    public List<UserView> listar() {
        return repo.findAll().stream().map(this::view).toList(); // ¡sin exponer el hash de password!
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CreateUserDTO dto) {
        if (dto.username() == null || dto.username().isBlank() || dto.password() == null || dto.password().isBlank())
            return ResponseEntity.badRequest().body("username/password requeridos");

        if (repo.findByUsername(dto.username().trim()).isPresent())
            return ResponseEntity.badRequest().body("El usuario ya existe");

        AppUser u = new AppUser();
        u.setUsername(dto.username().trim());
        u.setPassword(encoder.encode(dto.password()));
        u.setRole(dto.role() == null ? Role.CLIENTE : dto.role());
        u.setEnabled(Boolean.TRUE.equals(dto.enabled()));
        return ResponseEntity.ok(view(repo.save(u)));
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<?> cambiarRol(@PathVariable Long id, @RequestBody RoleDTO dto) {
        return repo.findById(id)
                .map(u -> { u.setRole(dto.role()); repo.save(u); return ResponseEntity.noContent().build(); })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/enabled")
    public ResponseEntity<?> cambiarEnabled(@PathVariable Long id, @RequestBody EnabledDTO dto) {
        return repo.findById(id)
                .map(u -> { u.setEnabled(Boolean.TRUE.equals(dto.enabled())); repo.save(u); return ResponseEntity.noContent().build(); })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Plantilla CSV
    @GetMapping(value = "/plantilla", produces = "text/csv")
    public ResponseEntity<String> plantilla() {
        String csv = "username;password;role;enabled\nana;1234;CLIENTE;true\n";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios_plantilla.csv")
                .body(csv);
    }

    // ---- Importar (texto o archivo)
    @PostMapping(value = "/import", consumes = { MediaType.TEXT_PLAIN_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public Map<String, Integer> importar(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestBody(required = false) String body) throws IOException {
        String text = (file != null ? new String(file.getBytes(), StandardCharsets.UTF_8) : body);
        int ok = 0, fail = 0;
        if (text != null) {
            for (String raw : text.split("\\r?\\n")) {
                String line = raw.trim();
                if (line.isBlank()) continue;
                String[] p = line.split(";");
                if (p[0].equalsIgnoreCase("username")) continue; // header
                try {
                    String username = p[0].trim();
                    String password = p[1].trim();
                    Role role = Role.valueOf(p[2].trim().toUpperCase(Locale.ROOT));
                    boolean enabled = Boolean.parseBoolean(p[3].trim());
                    if (repo.findByUsername(username).isPresent()) { fail++; continue; }
                    AppUser u = new AppUser();
                    u.setUsername(username);
                    u.setPassword(encoder.encode(password));
                    u.setRole(role);
                    u.setEnabled(enabled);
                    repo.save(u);
                    ok++;
                } catch (Exception ex) { fail++; }
            }
        }
        return Map.of("ok", ok, "fail", fail);
    }
}
