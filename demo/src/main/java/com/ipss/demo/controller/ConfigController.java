package com.ipss.demo.controller;

import com.ipss.demo.model.AppConfig;
import com.ipss.demo.service.AppConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    private final AppConfigService service;
    public ConfigController(AppConfigService service) { this.service = service; }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/horario")
    public Map<String,String> getHorario() {
        AppConfig c = service.getOrCreate();
        return Map.of("apertura", c.getApertura().toString(), "cierre", c.getCierre().toString());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/horario")
    public Map<String,String> setHorario(@RequestBody Map<String,String> body) {
        LocalTime a = LocalTime.parse(body.get("apertura")); // "HH:mm"
        LocalTime c = LocalTime.parse(body.get("cierre"));
        AppConfig cfg = service.update(a, c);
        return Map.of("apertura", cfg.getApertura().toString(), "cierre", cfg.getCierre().toString());
    }
}
