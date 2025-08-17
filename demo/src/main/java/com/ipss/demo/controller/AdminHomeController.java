package com.ipss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {

    // /admin -> templates/admin/index.html
    @GetMapping("/admin")
    public String adminHOME() { return "admin/index"; }

    // /admin/usuarios -> templates/admin/usuarios.html
    @GetMapping("/admin/usuarios")
    public String adminUsuarios() { return "admin/usuarios"; }

    // /admin/mesas -> templates/admin/mesas.html
    @GetMapping("/admin/mesas")
    public String adminMesas() { return "admin/mesas"; }

    // /admin/disponibilidad -> templates/admin/disponibilidad.html
    @GetMapping("/admin/disponibilidad")
    public String adminDisponibilidad() { return "admin/disponibilidad"; }
}
