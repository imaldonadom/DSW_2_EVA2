package com.ipss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roles")
public class RolePagesController {

    // Cambiamos la ruta para evitar conflicto con /admin
    @GetMapping("/admin")
    public String adminAlias() {
        // Si quieres que /roles/admin muestre lo mismo:
        return "admin/index"; // misma vista, otra RUTA
        // Si prefieres redirigir: return "redirect:/admin";
    }

    @GetMapping("/cliente")
    public String cliente() {
        return "cliente/index";
    }

    @GetMapping("/comensal")
    public String comensal() {
        return "comensal/index";
    }
}
